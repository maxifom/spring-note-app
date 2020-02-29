package com.maxifom.note_app.service;

import com.maxifom.note_app.domain.User;
import com.maxifom.note_app.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public UserService(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    public User newUser(String username, String password) throws Exception {
        boolean exists = userRepository.existsUserByUserName(username);
        if (exists) {
            throw new Exception(String.format("User with username %s exists", username));
        }

        return userRepository.save(new User(
                username,
                passwordService.encodePassword(password),
                true,
                "ROLE_USER"
        ));
    }
}
