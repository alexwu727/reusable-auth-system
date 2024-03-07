package io.github.alexwu727.authsystemuserservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.github.alexwu727.authsystemuserservice.User;
import io.github.alexwu727.authsystemuserservice.UserRepository;
import io.github.alexwu727.authsystemuserservice.exception.EmailAlreadyExistsException;
import io.github.alexwu727.authsystemuserservice.exception.UserNotFoundException;
import io.github.alexwu727.authsystemuserservice.exception.UsernameAlreadyExistsException;
import io.github.alexwu727.authsystemuserservice.util.UserMapper;
import io.github.alexwu727.authsystemuserservice.vo.RegistrationRequest;
import io.github.alexwu727.authsystemuserservice.vo.UserResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::UserToUserResponse).toList();
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        return userMapper.UserToUserResponse(user);
    }

    @Override
    public UserResponse findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
        return userMapper.UserToUserResponse(user);
    }

    @Override
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        return userMapper.UserToUserResponse(user);
    }

    @Override
    public UserResponse register(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Username " + request.getUsername() + " already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email " + request.getEmail() + " already exists");
        }
        User user = userMapper.RegistrationRequestToUser(request);
        userRepository.save(user);
        return userMapper.UserToUserResponse(user);
    }

    @Override
    public UserResponse patch(Long id, JsonPatch patch) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with id " + id + " not found");
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
        return userMapper.UserToUserResponse(updatedUser);
    }

    private User applyPatchToUser(JsonPatch patch, User targetUser) throws JsonPatchException, JsonProcessingException {
        JsonNode patchNode = objectMapper.convertValue(patch, JsonNode.class);
        for (JsonNode operation : patchNode) {
            if (operation.get("path") == null) {
                throw new JsonPatchException("Operation 'path' is required");
            }
            String path = operation.get("path").asText();
            if (List.of("createdAt").contains(path)) {
                throw new JsonPatchException("Operation on " + path + " is not allowed");
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

    @Override
    public void delete(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }
}
