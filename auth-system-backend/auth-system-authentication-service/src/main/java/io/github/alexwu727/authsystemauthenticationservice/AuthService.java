package io.github.alexwu727.authsystemauthenticationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.JsonPatchOperation;
import io.github.alexwu727.authsystemauthenticationservice.exception.*;
import io.github.alexwu727.authsystemauthenticationservice.user.Role;
import io.github.alexwu727.authsystemauthenticationservice.user.User;
import io.github.alexwu727.authsystemauthenticationservice.user.UserRepository;
import io.github.alexwu727.authsystemauthenticationservice.util.JwtUtil;
import io.github.alexwu727.authsystemauthenticationservice.vo.AuthResponse;
import io.github.alexwu727.authsystemauthenticationservice.vo.LoginRequest;
import io.github.alexwu727.authsystemauthenticationservice.vo.RegistrationRequest;
import io.github.alexwu727.authsystemauthenticationservice.vo.UpdateResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Validator validator;
    @Value("${user.service.base.url}")
    private String userServiceBaseUrl;

    public String createVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder verificationCode = new StringBuilder(String.valueOf(random.nextInt(1000000)));
        // add zero padding to the left
        while (verificationCode.length() < 6) {
            verificationCode.insert(0, "0");
        }
        return verificationCode.toString();
    }

    public AuthResponse register(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
            if (!user.isEnabled() && (user.getVerificationCodeExpiration().before(new Date()))) {
                userRepository.delete(user);
            } else {
                throw new UsernameAlreadyExistsException("Username " + request.getUsername() + " already exists");
            }
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            if (!user.isEnabled() && (user.getVerificationCodeExpiration().before(new Date()))) {
                userRepository.delete(user);
            } else {
                throw new EmailAlreadyExistsException("Email " + request.getEmail() + " already exists");
            }
        }
        String verificationCode = createVerificationCode();
        Date expiration = new Date(System.currentTimeMillis() + 2 * 60 * 1000);
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(request.getRole() == null ? Role.USER : request.getRole())
                .enabled(false)
                .verificationCode(verificationCode)
                .verificationCodeExpiration(expiration)
                .createdAt(new Date())
                .build();
        userRepository.save(user);
        sendVerificationCode(request.getUsername(), request.getEmail(), verificationCode);
        String token = jwtUtil.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public void sendVerificationCode(String username, String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("auth.system.io@gmail.com");
        message.setTo("alexwu727@gmail.com");
//        message.setTo(email);
        message.setSubject("Verification Code");
        message.setText("Hello, " + username + ". Your verification code is " + verificationCode);
        mailSender.send(message);
    }

    public void verifyUser(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode).orElseThrow(() -> new VerificationCodeNotFoundException("Verification code not found"));
        if (user.getVerificationCodeExpiration().before(new Date())) {
            throw new VerificationCodeExpiredException("Verification code has expired");
        }
        user.enable();
        storeInUserService(user);
        user.setVerificationCode(null);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        String username = request.getUsername();
        User user;
        if (username.contains("@")) {
            user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found"));
            username = user.getUsername();
        } else {
            user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        }
        if (!user.isEnabled()) {
            throw new UserNotVerifiedException("User is not verified");
        }
        user.setLastLoginDate(new Date());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        request.getPassword()
                )
        );

        String token = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public void storeInUserService(User user) {
        String url = userServiceBaseUrl + "register";
        restTemplate.postForEntity(url, user, User.class);
    }

    public UpdateResponse update(Long id, JsonPatch patch) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        if (!user.get().isEnabled()) {
            throw new UserNotVerifiedException("User is not verified");
        }
        User updatedUser;
        try {
            updatedUser = applyPatchToUser(patch, user.get());
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        validateUser(updatedUser);
        updatedUser.setLastUpdateDate(new Date());
        userRepository.save(updatedUser);
        return UpdateResponse.builder()
                .username(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole())
                .build();
    }

    private User applyPatchToUser(JsonPatch patch, User targetUser) throws JsonPatchException, JsonProcessingException {
        JsonNode patchNode = objectMapper.convertValue(patch, JsonNode.class);
        for (JsonNode operation : patchNode) {
            if (operation.get("path") == null) {
                throw new JsonPatchException("Missing path in operation");
            }
            String path = operation.get("path").asText();
            if (List.of("add", "copy", "move", "remove").contains(operation.get("op").asText())) {
                throw new JsonPatchException("Operation " + operation.get("op").asText() + " is not allowed");
            }
            switch (path) {
                case "/username" -> {
                    if (userRepository.existsByUsername(operation.get("value").asText())) {
                        throw new UsernameAlreadyExistsException("Username " + operation.get("value").asText() + " already exists");
                    }
                }
                case "/email" -> {
                    if (userRepository.existsByEmail(operation.get("value").asText())) {
                        throw new EmailAlreadyExistsException("Email " + operation.get("value").asText() + " already exists");
                    }
                }
                case "/role" -> {
                    if (operation.get("value") == null) {
                        throw new JsonPatchException("Missing value in operation");
                    }
                    List<String> validRoles = Arrays.stream(Role.values()).map(Enum::name).toList();
                    if (!validRoles.contains(operation.get("value").asText())) {
                        throw new JsonPatchException("Invalid role value " + operation.get("value").asText());
                    }
                }
                default ->
                        throw new JsonPatchException("Operation " + operation.get("op").asText() + " on path " + operation.get("path").asText() + " is not allowed");
            }
        }
        JsonNode patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
        return objectMapper.treeToValue(patched, User.class);
    }

    private void validateUser(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
