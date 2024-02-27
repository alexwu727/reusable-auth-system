package io.github.alexwu727.authsystemauthenticationservice;

import com.github.fge.jsonpatch.JsonPatch;
import io.github.alexwu727.authsystemauthenticationservice.vo.AuthResponse;
import io.github.alexwu727.authsystemauthenticationservice.vo.LoginRequest;
import io.github.alexwu727.authsystemauthenticationservice.vo.RegistrationRequest;
import io.github.alexwu727.authsystemauthenticationservice.vo.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Validated RegistrationRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String verificationCode) {
        authService.verifyUser(verificationCode);
        return ResponseEntity.ok("User verified successfully");
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<UpdateResponse> update(@PathVariable Long id, @RequestBody JsonPatch patch) {
        return ResponseEntity.ok(authService.update(id, patch));
    }
}
