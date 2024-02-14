package io.github.alexwu727.authsystemuserservice.vo;

import io.github.alexwu727.authsystemuserservice.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPatch {
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @Email(message = "Email is not valid")
    private String email;

    private Role role;
}
