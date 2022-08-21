package com.thiendz.example.springsocket.model;

import lombok.Data;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String username;
    String password;
    String role;
    boolean status;
    Long createAt;
    Long updateAt;
    Long deleteAt;
}
