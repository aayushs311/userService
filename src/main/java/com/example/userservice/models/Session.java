package com.example.userservice.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Session {
    private String token;
    private Date expiringAT;
    private User user;
}
