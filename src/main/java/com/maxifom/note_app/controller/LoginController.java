package com.maxifom.note_app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String showLoginForm(Authentication authentication) {
        return authentication == null ? "login" : "redirect:/";
    }
}