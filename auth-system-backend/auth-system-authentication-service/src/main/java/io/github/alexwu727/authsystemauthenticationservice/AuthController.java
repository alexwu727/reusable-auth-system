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
    public ResponseEntity<MessageResponse> register(@RequestBody @Validated RegistrationRequest request) {
        authService.register(request);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader(value = "User-Id") String userId) {
        System.out.println("User-Id: " + userId);
        long id = Long.parseLong(userId);
        return ResponseEntity.ok(authService.refreshToken(id));
    }

    @PostMapping("verify")
    public ResponseEntity<MessageResponse> verify(@RequestBody @Validated VerificationRequest request) {
        authService.verifyUser(request);
        return ResponseEntity.ok(new MessageResponse("User verified successfully"));
    }

    @PostMapping("resend-verification-code")
    public ResponseEntity<MessageResponse> resendVerificationCode(@RequestParam String email) {
        authService.resendVerificationCode(email);
        return ResponseEntity.ok(new MessageResponse("Verification code sent successfully"));
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<UpdateResponse> update(@PathVariable Long id, @RequestBody JsonPatch patch) {
        return ResponseEntity.ok(authService.update(id, patch));
    }

    @PostMapping("update-password")
    public ResponseEntity<MessageResponse> updatePassword(@RequestBody @Validated UpdatePasswordRequest request) {
        authService.updatePassword(request);
        return ResponseEntity.ok(new MessageResponse("Password updated successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestParam String email) {
        authService.createPasswordResetVerificationCode(email);
        return ResponseEntity.ok(new MessageResponse("Password reset code sent successfully"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody @Validated ResetPasswordRequest request) {
        authService.verifyAndResetPassword(request);
        return ResponseEntity.ok(new MessageResponse("Password reset successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        authService.delete(id);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
    }
}
