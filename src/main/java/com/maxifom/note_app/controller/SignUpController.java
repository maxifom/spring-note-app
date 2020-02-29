package com.maxifom.note_app.controller;

import com.maxifom.note_app.domain.MyUserDetails;
import com.maxifom.note_app.domain.User;
import com.maxifom.note_app.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {
    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignUpForm(Authentication authentication) {
        return authentication == null ? "signup" : "redirect:/";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password, @RequestParam String password_confirm) throws Exception {
        if (username.isEmpty() || password.isEmpty()) {
            throw new Exception("Required fields missing");
        }
        if (!password.equals(password_confirm)) {
            throw new Exception("Passwords don't match");
        }

        User user = userService.newUser(username, password);

        MyUserDetails principal = new MyUserDetails(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                principal,
                principal.getPassword(),
                principal.getAuthorities()
        ));

        return "redirect:/";
    }
}
