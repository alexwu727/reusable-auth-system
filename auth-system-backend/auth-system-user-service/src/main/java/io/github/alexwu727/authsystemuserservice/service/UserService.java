package io.github.alexwu727.authsystemuserservice.service;

import io.github.alexwu727.authsystemuserservice.User;
import org.springframework.data.util.Pair;

import java.util.List;

public interface UserService {
    List<User> findAll();
    Pair<User, String> register(User user);
    User findByUsername(String username);
    User findById(Long id);
    User update(Long id, User user);
    User patch(Long id, User user);
    void delete(Long id);
    void test();
}
