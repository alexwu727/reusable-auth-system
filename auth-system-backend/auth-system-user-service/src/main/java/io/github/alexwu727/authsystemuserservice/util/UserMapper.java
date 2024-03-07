package io.github.alexwu727.authsystemuserservice.util;

import io.github.alexwu727.authsystemuserservice.User;
import io.github.alexwu727.authsystemuserservice.vo.UserPatch;
import io.github.alexwu727.authsystemuserservice.vo.RegistrationRequest;
import io.github.alexwu727.authsystemuserservice.vo.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User RegistrationRequestToUser(RegistrationRequest registrationRequest) {
        return User.builder()
                .id(registrationRequest.getId())
                .username(registrationRequest.getUsername())
                .email(registrationRequest.getEmail())
                .role(registrationRequest.getRole())
                .createdAt(registrationRequest.getCreatedAt())
                .build();
    }

    public UserResponse UserToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(String.valueOf(user.getRole()))
                .build();
    }

    public User UserPatchToUser(UserPatch userPatch) {
        User user = new User();
        user.setUsername(userPatch.getUsername());
        user.setEmail(userPatch.getEmail());
        user.setRole(userPatch.getRole());
        return user;
    }
}
