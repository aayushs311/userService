package com.example.userservice.services;

import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public boolean signUp(String email, String password) {
        return true;
    }

    public String login(String email, String password) {
        return "token";
    }
}
