package com.example.userservice.services;

import com.example.userservice.Repositories.UserRepository;
import com.example.userservice.exceptions.UserAlreadyExistException;
import com.example.userservice.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public boolean signUp(String email, String password) throws UserAlreadyExistException {
        if(userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistException("User with E mail " + email + " already exists.");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return true;
    }

    public String login(String email, String password) {
        return "token";
    }
}
