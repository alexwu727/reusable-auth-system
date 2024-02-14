package io.github.alexwu727.authsystemuserservice.service;

import io.github.alexwu727.authsystemuserservice.User;
import io.github.alexwu727.authsystemuserservice.UserRepository;
import io.github.alexwu727.authsystemuserservice.exception.EmailAlreadyExistsException;
import io.github.alexwu727.authsystemuserservice.exception.UserNotFoundException;
import io.github.alexwu727.authsystemuserservice.exception.UsernameAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final String authServiceBaseUrl;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public UserServiceImpl(@Value("${auth.service.base.url}") String authServiceBaseUrl, UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.authServiceBaseUrl = authServiceBaseUrl;
    }

    @Override
    public void test() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(authServiceBaseUrl + "test", String.class);
            System.out.println("test");
            System.out.println(response.getBody());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // list all user
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Pair<User, String> register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Username " + user.getUsername() + " already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " already exists");
        }
        String url = authServiceBaseUrl + "register";
        System.out.println(url);
        Map<String, String> request = Map.of(
                "username", user.getUsername(),
                "password", user.getPassword(),
                "email", user.getEmail(),
                "role", user.getRole().toString()
        );
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        String token = (String) response.getBody().get("token");
        assert token != null;
        return Pair.of(userRepository.save(user), token);
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }
        return user.get();
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        return user.get();
    }

    @Override
    public User update(Long id, User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Username " + user.getUsername() + " already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " already exists");
        }
        User userFromDB = userOptional.get();
        userFromDB.setUsername(user.getUsername());
        userFromDB.setEmail(user.getEmail());
        return userRepository.save(userFromDB);
    }

    @Override
    public User patch(Long id, User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        User userFromDB = userOptional.get();
        if (user.getUsername() != null) {
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new UsernameAlreadyExistsException("Username " + user.getUsername() + " already exists");
            }
            userFromDB.setUsername(user.getUsername());
        }
        if (user.getEmail() != null) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new UsernameAlreadyExistsException("Email " + user.getEmail() + " already exists");
            }
            userFromDB.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            userFromDB.setPassword(user.getPassword());
        }
        return userRepository.save(userFromDB);
    }

    @Override
    public void delete(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }
}
