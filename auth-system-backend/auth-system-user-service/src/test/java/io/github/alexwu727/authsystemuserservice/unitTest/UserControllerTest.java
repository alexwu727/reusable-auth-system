package io.github.alexwu727.authsystemuserservice.unitTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.github.alexwu727.authsystemuserservice.Role;
import io.github.alexwu727.authsystemuserservice.User;
import io.github.alexwu727.authsystemuserservice.UserController;
import io.github.alexwu727.authsystemuserservice.exception.UserNotFoundException;
import io.github.alexwu727.authsystemuserservice.service.UserService;
import io.github.alexwu727.authsystemuserservice.util.UserMapper;
import io.github.alexwu727.authsystemuserservice.vo.RegistrationResponse;
import io.github.alexwu727.authsystemuserservice.vo.RegistrationRequest;
import io.github.alexwu727.authsystemuserservice.vo.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ObjectMapper objectMapper;

    private User user;
    private UserResponse userResponse;
    private RegistrationRequest registrationRequest;
    private RegistrationResponse registrationResponse;
    private JsonPatch patch;
    private String token;

    @BeforeEach
    void setup() {
        user = new User(1L, "alex", "alex@example.com", Role.USER, new Date(), new Date());
        userResponse = new UserResponse(1L, "alex", "alex@example.com", "USER");
        registrationRequest = new RegistrationRequest(1L, "alex", "alex@example.com", Role.USER, new Date());
        token = "token";
        registrationResponse = new RegistrationResponse(userResponse, token);
        // json patch with replace username to "alexwu"
        String patchString = "[{\"op\":\"replace\",\"path\":\"/username\",\"value\":\"alexwu\"}]";
        try {
            patch = JsonPatch.fromJson(objectMapper.readTree(patchString));
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllUsers_ReturnsAllUsers() {
        // Arrange
        when(userService.findAll()).thenReturn(List.of(userResponse));

        // Act
        ResponseEntity<List<UserResponse>> result = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(List.of(userResponse), result.getBody());
    }

    @Test
    void register_WithValidUserRegistration_ReturnsCreatedUser() {
        // Arrange
        when(userService.register(any(RegistrationRequest.class))).thenReturn(userResponse);

        // Act
        ResponseEntity<UserResponse> result = userController.register(registrationRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(userResponse, result.getBody());
    }

    @Test
    void getUserById_WithExistingId_ReturnsUser() {
        // Arrange
        when(userService.findById(1L)).thenReturn(userResponse);

        // Act
        ResponseEntity<UserResponse> result = userController.getUserById(1L);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userResponse, result.getBody());
    }

    @Test
    void getUserById_WithNonExistingId_ThrowsUserNotFoundException() {
        // Arrange
        when(userService.findById(1L)).thenThrow(new UserNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userController.getUserById(1L));
    }

    @Test
    void getUserByUsername_WithExistingUsername_ReturnsUser() {
        // Arrange
        when(userService.findByUsername("alex")).thenReturn(userResponse);

        // Act
        ResponseEntity<UserResponse> result = userController.getUserByUsername("alex");

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userResponse, result.getBody());
    }

    @Test
    void getUserByUsername_WithNonExistingUsername_ThrowsUserNotFoundException() {
        // Arrange
        when(userService.findByUsername("alex")).thenThrow(new UserNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userController.getUserByUsername("alex"));
    }

    @Test
    void patchUser_WithExistingId_ReturnsUpdatedUser() {
        // Arrange
        when(userService.patch(any(Long.class), any(JsonPatch.class))).thenReturn(userResponse);

        // Act
        ResponseEntity<UserResponse> result = userController.patchUser(1L, patch);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userResponse, result.getBody());
    }

    @Test
    void patchUser_WithNonExistingId_ThrowsUserNotFoundException() {
        // Arrange
        when(userService.patch(any(Long.class), any(JsonPatch.class))).thenThrow(new UserNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userController.patchUser(1L, new JsonPatch(List.of())));
    }

    @Test
    void deleteUser_WithExistingId_ReturnsDeletedUser() {
        // Arrange
        when(userService.findById(1L)).thenReturn(userResponse);

        // Act
        ResponseEntity<UserResponse> result = userController.deleteUser(1L);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userResponse, result.getBody());
    }

    @Test
    void deleteUser_WithNonExistingId_ThrowsUserNotFoundException() {
        // Arrange
        when(userService.findById(1L)).thenThrow(new UserNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userController.deleteUser(1L));
    }
}