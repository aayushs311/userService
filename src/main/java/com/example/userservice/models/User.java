package com.example.userservice.models;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class User {
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();
}
