package com.example.userservice.models;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();
}
