package io.github.alexwu727.authsystemuserservice;

import com.github.fge.jsonpatch.JsonPatch;
import io.github.alexwu727.authsystemuserservice.service.UserService;
import io.github.alexwu727.authsystemuserservice.util.UserMapper;
import io.github.alexwu727.authsystemuserservice.vo.RegistrationResponse;
import io.github.alexwu727.authsystemuserservice.vo.UserPatch;
import io.github.alexwu727.authsystemuserservice.vo.RegistrationRequest;
import io.github.alexwu727.authsystemuserservice.vo.UserResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegistrationRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("/")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<UserResponse> patchUser(@PathVariable("id") Long id, @RequestBody JsonPatch patch) {
        return ResponseEntity.ok(userService.patch(id, patch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable("id") Long id) {
        UserResponse userResponse = userService.findById(id);
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping("info")
    public ResponseEntity<UserResponse> getUserInfo(@RequestHeader(value = "User-Id") String userId) {
        long id = Long.parseLong(userId);
        return ResponseEntity.ok(userService.findById(id));
    }
}
