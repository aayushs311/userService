package com.example.userservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class User extends BaseModel {
    private String email;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();
}
