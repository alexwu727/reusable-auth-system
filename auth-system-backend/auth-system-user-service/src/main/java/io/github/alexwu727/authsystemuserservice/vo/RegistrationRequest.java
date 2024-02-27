package io.github.alexwu727.authsystemuserservice.vo;

import io.github.alexwu727.authsystemuserservice.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class RegistrationRequest {

    private Long id;

    private String username;

    private String email;

    private Role role;

    private Date createdAt;
}
