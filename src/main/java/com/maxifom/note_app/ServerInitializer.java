package com.maxifom.note_app;

import com.maxifom.note_app.domain.User;
import com.maxifom.note_app.repository.UserRepository;
import com.maxifom.note_app.service.PasswordService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static java.lang.System.getenv;

@Component
public class ServerInitializer implements ApplicationRunner {
    private final UserRepository userRepository;

    public ServerInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        PasswordService encoder = new PasswordService();
        String password;
        password = getenv("ADMIN_PASSWORD");
        if (password == null || password.isEmpty()) {
            throw new Exception("no password specified for admin");
        }
        userRepository.save(new User("root", encoder.encodePassword(password), true, "ROLE_ADMIN"));
    }
}
