package io.github.alexwu727.authsystemauthenticationservice;

import com.github.fge.jsonpatch.JsonPatch;
import io.github.alexwu727.authsystemauthenticationservice.vo.*;
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

    @PostMapping("verify")
    public ResponseEntity<String> verify(@RequestBody @Validated VerificationRequest request) {
        authService.verifyUser(request);
        return ResponseEntity.ok("User verified successfully");
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<UpdateResponse> update(@PathVariable Long id, @RequestBody JsonPatch patch) {
        return ResponseEntity.ok(authService.update(id, patch));
    }

    @PostMapping("update-password")
    public ResponseEntity<String> updatePassword(@RequestBody @Validated UpdatePasswordRequest request) {
        authService.updatePassword(request);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        authService.createPasswordResetVerificationCode(email);
        return ResponseEntity.ok("Password reset verification code sent successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Validated ResetPasswordRequest request) {
        authService.verifyAndResetPassword(request);
        return ResponseEntity.ok("Password reset successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        authService.delete(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
