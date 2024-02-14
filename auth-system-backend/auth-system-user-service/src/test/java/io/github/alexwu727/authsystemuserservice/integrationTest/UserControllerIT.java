package io.github.alexwu727.authsystemuserservice.integrationTest;

import io.github.alexwu727.authsystemuserservice.Role;
import io.github.alexwu727.authsystemuserservice.User;
import io.github.alexwu727.authsystemuserservice.exception.UserNotFoundException;
import io.github.alexwu727.authsystemuserservice.exception.UsernameAlreadyExistsException;
import io.github.alexwu727.authsystemuserservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user1;
    private User user2;
    private List<User> users;
    private String userRegistrationJson;
    private String token;
    @BeforeEach
    void setup() {
        user1 = new User(1L, "alex", "12345678", "alex@example.com", Role.USER, new Date());
        user2 = new User(2L, "bob", "12345678", "bob@example.com", Role.ADMIN, new Date());
        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        userRegistrationJson = "{\n" +
                "\"username\": \"alex\",\n" +
                "\"password\": \"12345678\",\n" +
                "\"email\": \"alex@example.com\"" +
                "\n}";
        token = "token";
    }

    @Test
    void getAllUsers_ReturnsAllUsers() throws Exception {
        when(userService.findAll()).thenReturn(users);
        mockMvc.perform(get("/api/v1/users/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is(user1.getUsername())))
                .andExpect(jsonPath("$[0].email", is(user1.getEmail())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].username", is(user2.getUsername())))
                .andExpect(jsonPath("$[1].email", is(user2.getEmail())));
    }

    @Test
    void getUserById_WithValidId_ReturnsUser() throws Exception {
        when(userService.findById(1L)).thenReturn(user1);
        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is(user1.getUsername())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())));
    }

    @Test
    void getUserById_WithInvalidId_ReturnsNotFound() throws Exception {
        when(userService.findById(1L)).thenThrow(new UserNotFoundException("User not found"));
        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserByUsername_WithValidUsername_ReturnsUser() throws Exception {
        when(userService.findByUsername("alex")).thenReturn(user1);
        mockMvc.perform(get("/api/v1/users/username/alex"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is(user1.getUsername())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())));
    }

    @Test
    void getUserByUsername_WithInvalidUsername_ReturnsNotFound() throws Exception {
        when(userService.findByUsername("alex")).thenThrow(new UserNotFoundException("User not found"));
        mockMvc.perform(get("/api/v1/users/username/alex"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User not found")));
    }

    @Test
    void register_WithValidUserRegistration_ReturnsCreatedUser() throws Exception {
        when(userService.register(any(User.class))).thenReturn(Pair.of(user1, token));
        mockMvc.perform(post("/api/v1/users/")
                        .contentType("application/json")
                        .content(userRegistrationJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userResponse.id", is(1)))
                .andExpect(jsonPath("$.userResponse.username", is(user1.getUsername())))
                .andExpect(jsonPath("$.userResponse.email", is(user1.getEmail())))
                .andExpect(jsonPath("$.token", is(token)));
    }

    @Test
    void register_WithInvalidUserRegistration_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/users/")
                        .contentType("application/json")
                        .content("{\n" +
                                "\"username\": \"alex\",\n" +
                                "\"password\": \"123456\",\n" +
                                "\"email\": \"alex@example.com\"" +
                                "\n}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andDo(print()); // print out the request body
    }

    @Test
    void register_WithExistingUsername_ReturnsBadRequest() throws Exception {
        when(userService.register(any(User.class))).thenThrow(new UsernameAlreadyExistsException("Username " + user1.getUsername() + " already exists"));
        mockMvc.perform(post("/api/v1/users/")
                        .contentType("application/json")
                        .content(userRegistrationJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Username " + user1.getUsername() + " already exists")))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameAlreadyExistsException))
                .andDo(print()); // print out the request body
    }

    @Test
    void register_WithExistingEmail_ReturnsBadRequest() throws Exception {
        when(userService.register(any(User.class))).thenThrow(new UsernameAlreadyExistsException("Email " + user1.getEmail() + " already exists"));
        mockMvc.perform(post("/api/v1/users/")
                        .contentType("application/json")
                        .content(userRegistrationJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Email " + user1.getEmail() + " already exists")))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameAlreadyExistsException))
                .andDo(print()); // print out the request body
    }

    @Test
    void updateUser_WithValidUser_ReturnsUpdatedUser() throws Exception {
        when(userService.update(any(), any(User.class))).thenReturn(user1);
        mockMvc.perform(put("/api/v1/users/1")
                        .contentType("application/json")
                        .content(userRegistrationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is(user1.getUsername())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())));
    }

    @Test
    void updateUser_WithExistingUsername_ReturnsBadRequest() throws Exception {
        when(userService.update(any(), any(User.class))).thenThrow(new UsernameAlreadyExistsException("Username " + user1.getUsername() + " already exists"));
        mockMvc.perform(put("/api/v1/users/1")
                        .contentType("application/json")
                        .content(userRegistrationJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Username " + user1.getUsername() + " already exists")))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameAlreadyExistsException))
                .andDo(print()); // print out the request body
    }

    @Test
    void updateUser_WithExistingEmail_ReturnsBadRequest() throws Exception {
        when(userService.update(any(), any(User.class))).thenThrow(new UsernameAlreadyExistsException("Email " + user1.getEmail() + " already exists"));
        mockMvc.perform(put("/api/v1/users/1")
                        .contentType("application/json")
                        .content(userRegistrationJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Email " + user1.getEmail() + " already exists")))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameAlreadyExistsException))
                .andDo(print()); // print out the request body
    }

    @Test
    void deleteUser_WithValidId_ReturnsNoContent() throws Exception {
        when(userService.findById(1L)).thenReturn(user1);
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_WithInvalidId_ReturnsNotFound() throws Exception {
        when(userService.findById(1L)).thenThrow(new UserNotFoundException("User not found"));
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andDo(print()); // print out the request body
    }
}
