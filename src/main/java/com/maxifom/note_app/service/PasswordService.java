package com.maxifom.note_app.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final BCryptPasswordEncoder encoder;

    public PasswordService() {
        this.encoder = new BCryptPasswordEncoder(4);
    }

    public String encodePassword(CharSequence password) {
        return encoder.encode(password);
    }

    public PasswordEncoder getEncoder() {
        return this.encoder;
    }
}
