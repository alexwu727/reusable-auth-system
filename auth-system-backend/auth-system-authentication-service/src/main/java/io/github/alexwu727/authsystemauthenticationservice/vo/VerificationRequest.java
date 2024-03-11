package io.github.alexwu727.authsystemauthenticationservice.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationRequest {
    @NotEmpty(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotEmpty(message = "Verification code is required")
    private String verificationCode;
}