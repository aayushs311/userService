package com.example.userservice.services;

import com.example.userservice.Repositories.SessionRepository;
import com.example.userservice.Repositories.UserRepository;
import com.example.userservice.exceptions.UserAlreadyExistException;
import com.example.userservice.exceptions.UserNotFoundException;
import com.example.userservice.exceptions.WrongPasswordException;
import com.example.userservice.models.Session;
import com.example.userservice.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class AuthService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SecretKey key = Jwts.SIG.HS256.key().build();
    private SessionRepository sessionRepository;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sessionRepository = sessionRepository;
    }
    public boolean signUp(String email, String password) throws UserAlreadyExistException {
        if(userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistException("User with E-mail " + email + " already exists.");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return true;
    }

    public String login(String email, String password) throws UserNotFoundException, WrongPasswordException {
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()) {
            throw new UserNotFoundException("User with E-mail " + email + " not found");
        }
        boolean matches = bCryptPasswordEncoder.matches(
                password,
                user.get().getPassword()
        );
        if(matches) {
            String token = createJwtToken(user.get().getId(), new ArrayList<>(), user.get().getEmail());
            Session session = new Session();
            session.setToken(token);
            session.setUser(user.get());

            LocalDateTime futureDateTime = LocalDateTime.now().plusDays(30);
            Date date = Date.from(
                    futureDateTime.atZone(ZoneId.systemDefault()).toInstant()
            );
            session.setExpiringAT(date);

            sessionRepository.save(session);

            return token;
        } else {
            throw new WrongPasswordException("Wrong password");
        }

    }

    private String createJwtToken(Long userId, List<String> roles, String email) {
        Map<String, Object> dataInJwt = new HashMap<>();

        LocalDateTime futureDateTime = LocalDateTime.now().plusDays(30);
        Date date = Date.from(
                futureDateTime.atZone(ZoneId.systemDefault()).toInstant()
        );

        dataInJwt.put("user_id", userId);
        dataInJwt.put("roles", roles);
        dataInJwt.put("email", email);

        String token = Jwts.builder()
                .claims(dataInJwt)
                .expiration(date)
                .issuedAt(new Date())
                .signWith(key)
                .compact();

        return token;
    }

    public boolean validate(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            // We can do validations as well:
            Date expirytAt = claims.getPayload().getExpiration();
            Long userId = claims.getPayload().get("user_id", Long.class);

            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
