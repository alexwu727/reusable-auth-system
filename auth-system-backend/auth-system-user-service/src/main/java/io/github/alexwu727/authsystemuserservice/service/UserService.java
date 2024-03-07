package io.github.alexwu727.authsystemuserservice.service;

import com.github.fge.jsonpatch.JsonPatch;
import io.github.alexwu727.authsystemuserservice.User;
import io.github.alexwu727.authsystemuserservice.vo.RegistrationRequest;
import io.github.alexwu727.authsystemuserservice.vo.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();
    UserResponse findById(Long id);
    UserResponse findByUsername(String username);
    UserResponse findByEmail(String email);
    UserResponse register(RegistrationRequest request);
    UserResponse patch(Long id, JsonPatch patch);
    void delete(Long id);
}
