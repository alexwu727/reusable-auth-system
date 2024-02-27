package io.github.alexwu727.authsystemuserservice.util;

import io.github.alexwu727.authsystemuserservice.User;
import io.github.alexwu727.authsystemuserservice.vo.UserPatch;
import io.github.alexwu727.authsystemuserservice.vo.RegistrationRequest;
import io.github.alexwu727.authsystemuserservice.vo.UserResponse;

public class UserMapper {
    public User UserRegistrationToUser(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setRole(registrationRequest.getRole());
        return user;
    }

    public UserResponse UserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole().toString());
        return userResponse;
    }

    public User UserPatchToUser(UserPatch userPatch) {
        User user = new User();
        user.setUsername(userPatch.getUsername());
        user.setEmail(userPatch.getEmail());
        user.setRole(userPatch.getRole());
        return user;
    }
}
