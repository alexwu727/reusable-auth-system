package io.github.alexwu727.authsystemuserservice;

import io.github.alexwu727.authsystemuserservice.service.UserService;
import io.github.alexwu727.authsystemuserservice.util.UserMapper;
import io.github.alexwu727.authsystemuserservice.vo.RegistrationResponse;
import io.github.alexwu727.authsystemuserservice.vo.UserPatch;
import io.github.alexwu727.authsystemuserservice.vo.UserRegistration;
import io.github.alexwu727.authsystemuserservice.vo.UserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/discovery")
    public void printServiceInstances() {
        List<ServiceInstance> instances = discoveryClient.getInstances("authentication-service");
        for (ServiceInstance instance : instances) {
            System.out.println("Instance: " + instance.getServiceId() + " - " + instance.getUri());
        }
    }

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        this.userMapper = new UserMapper();
    }

    @GetMapping("/")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserResponse> userResponses = users.stream().map(userMapper::UserToUserResponse).toList();
        return ResponseEntity.ok(userResponses);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody @Valid UserRegistration userRegistration) {
        User user = userMapper.UserRegistrationToUser(userRegistration);
        Pair<User, String> userStringPair = userService.register(user);
        UserResponse userResponse = userMapper.UserToUserResponse(userStringPair.getFirst());
        String token = userStringPair.getSecond();
        RegistrationResponse registrationResponse = new RegistrationResponse(userResponse, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
    }

    // get user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        UserResponse userResponse = userMapper.UserToUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    // get user by username
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable("username") String username) {
        User user = userService.findByUsername(username);
        UserResponse userResponse = userMapper.UserToUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserRegistration userRegistration) {
        User user = userMapper.UserRegistrationToUser(userRegistration);
        User updatedUser = userService.update(id, user);
        UserResponse userResponse = userMapper.UserToUserResponse(updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> patchUser(@PathVariable("id") Long id, @RequestBody @Valid UserPatch userPatch) {
        User user = userMapper.UserPatchToUser(userPatch);
        User updatedUser = userService.patch(id, user);
        UserResponse userResponse = userMapper.UserToUserResponse(updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        userService.delete(id);
        UserResponse userResponse = userMapper.UserToUserResponse(user);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping("info")
    public ResponseEntity<UserResponse> getUserInfo(@RequestHeader(value = "User-Id") String userId) {
        long id = Long.parseLong(userId);
        User user = userService.findById(id);
        UserResponse userResponse = userMapper.UserToUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }
}
