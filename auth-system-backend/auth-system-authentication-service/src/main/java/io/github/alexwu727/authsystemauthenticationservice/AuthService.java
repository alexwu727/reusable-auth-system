package io.github.alexwu727.authsystemauthenticationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.github.alexwu727.authsystemauthenticationservice.exception.*;
import io.github.alexwu727.authsystemauthenticationservice.user.*;
import io.github.alexwu727.authsystemauthenticationservice.util.CodeUtil;
import io.github.alexwu727.authsystemauthenticationservice.util.JwtUtil;
import io.github.alexwu727.authsystemauthenticationservice.vo.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordResetVerificationCodeRepository passwordResetVerificationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodeUtil codeUtil;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Validator validator;
    @Value("${user.service.base.url}")
    private String userServiceBaseUrl;

    public AuthResponse register(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
            if (!user.isVerified() && (user.getVerificationCodeExpiration().before(new Date()))) {
                userRepository.delete(user);
            } else {
                throw new UsernameAlreadyExistsException("Username " + request.getUsername() + " already exists");
            }
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            if (!user.isVerified() && (user.getVerificationCodeExpiration().before(new Date()))) {
                userRepository.delete(user);
            } else {
                throw new EmailAlreadyExistsException("Email " + request.getEmail() + " already exists");
            }
        }
        String verificationCode = codeUtil.generateCode();
        Date expiration = new Date(System.currentTimeMillis() + 2 * 60 * 1000);
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(request.getRole() == null ? Role.USER : request.getRole())
                .verified(false)
                .enabled(false)
                .verificationCode(verificationCode)
                .verificationCodeExpiration(expiration)
                .createdAt(new Date())
                .build();
        userRepository.save(user);
        codeUtil.sendCode(request.getUsername(), request.getEmail(), verificationCode, true);
        String token = jwtUtil.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public void verifyUser(String email, String verificationCode) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (user.isVerified()) {
            throw new UserAlreadyVerifiedException("User is already verified");
        }
        if (!user.getVerificationCode().equals(verificationCode)) {
            throw new VerificationCodeMismatchException("Verification code does not match");
        }
        if (user.getVerificationCodeExpiration().before(new Date())) {
            throw new VerificationCodeExpiredException("Verification code has expired");
        }
        user.enable();
        user.setVerified(true);
        storeInUserService(user);
        user.setVerificationCode(null);
        userRepository.save(user);
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

    private void storeInUserService(User user) {
        String url = userServiceBaseUrl + "register";
        restTemplate.postForEntity(url, user, User.class);
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
        if (!user.isVerified()) {
            throw new UserNotVerifiedException("User is not verified");
        }
        user.setLastLoginDate(new Date());
        userRepository.save(user);

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

    public UpdateResponse update(Long id, JsonPatch patch) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        if (!user.get().isVerified()) {
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

    public void delete(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        if (!user.get().isVerified()) {
            throw new UserNotVerifiedException("User is not verified");
        }
        userRepository.delete(user.get());
    }

    public void createPasswordResetVerificationCode(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.isVerified()) {
            throw new UserNotVerifiedException("User is not verified");
        }
        PasswordResetVerificationCode token = PasswordResetVerificationCode.builder()
                .user(user)
                .verificationCode(codeUtil.generateCode())
                .expiryDate(new Date(System.currentTimeMillis() + PasswordResetVerificationCode.EXPIRATION * 60 * 1000))
                .build();
        passwordResetVerificationCodeRepository.save(token);
        codeUtil.sendCode(user.getUsername(), user.getEmail(), token.getVerificationCode(), false);
    }

    public void verifyPasswordResetVerificationCode(String email, String verificationCode) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.isVerified()) {
            throw new UserNotVerifiedException("User is not verified");
        }
        PasswordResetVerificationCode passwordResetVerificationCode = passwordResetVerificationCodeRepository.findByUser(user).orElseThrow(() -> new VerificationCodeNotFoundException("Verification code not found"));
        if (!passwordResetVerificationCode.getVerificationCode().equals(verificationCode)) {
            throw new VerificationCodeMismatchException("Verification code does not match");
        }
        if (passwordResetVerificationCode.getExpiryDate().before(new Date())) {
            throw new VerificationCodeExpiredException("Verification code has expired");
        }
    }

    public void verifyAndResetPassword(ResetPasswordRequest request) {
        verifyPasswordResetVerificationCode(request.getEmail(), request.getVerificationCode());
        resetPassword(request.getEmail(), request.getNewPassword());
    }

    public void updatePassword(UpdatePasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.isVerified()) {
            throw new UserNotVerifiedException("User is not verified");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new PasswordMismatchException("Password does not match");
        }
        resetPassword(request.getEmail(), request.getNewPassword());
    }

    private void resetPassword(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.isVerified()) {
            throw new UserNotVerifiedException("User is not verified");
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
