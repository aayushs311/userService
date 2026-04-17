package com.example.userservice.controllers;

import com.example.userservice.dtos.*;
import com.example.userservice.services.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign_up")
    public ResponseEntity<SignUpResponseDto> signUp(SignUpRequestDto request) {
        SignUpResponseDto response = new SignUpResponseDto();
        if(authService.signUp(request.getEmail(), request.getPassword())) {
            response.setRequestStatus(RequestStatus.SUCCESS);
        } else {
            response.setRequestStatus(RequestStatus.FAILURE);
        }
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        LoginResponseDto loginDto = new LoginResponseDto();
        loginDto.setRequestStatus(RequestStatus.SUCCESS);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        ResponseEntity<LoginResponseDto> response = new ResponseEntity<>(
                loginDto, headers, HttpStatus.OK
        );
        return response;
    }

}
