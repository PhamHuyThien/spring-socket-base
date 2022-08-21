package com.thiendz.example.springsocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(indexes = @Index(columnList = "username"))
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
