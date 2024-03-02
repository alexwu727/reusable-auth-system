package io.github.alexwu727.authsystemauthenticationservice.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordRequest {
    @NotEmpty(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotEmpty(message = "Verification code is required")
    private String verificationCode;

    @NotEmpty(message = "New password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String newPassword;
}
