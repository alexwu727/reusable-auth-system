package io.github.alexwu727.authsystemuserservice.vo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrationResponse {
    private UserResponse userResponse;
    private String token;
}
