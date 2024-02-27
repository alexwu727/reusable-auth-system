package io.github.alexwu727.authsystemauthenticationservice.vo;

import io.github.alexwu727.authsystemauthenticationservice.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResponse {
    private String username;
    private String email;
    private Role role;
}
