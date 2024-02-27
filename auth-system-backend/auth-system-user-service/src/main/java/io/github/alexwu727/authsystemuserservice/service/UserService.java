package io.github.alexwu727.authsystemuserservice.service;

import io.github.alexwu727.authsystemuserservice.User;
import io.github.alexwu727.authsystemuserservice.vo.RegistrationRequest;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> findAll();
    User register(RegistrationRequest request);
    User findByUsername(String username);
    User findById(Long id);
    User update(Long id, User user);
    User patch(Long id, Map<String, Object> request);
    void delete(Long id);
}
