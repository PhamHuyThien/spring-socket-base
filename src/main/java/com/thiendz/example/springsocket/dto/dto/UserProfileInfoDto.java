package com.thiendz.example.springsocket.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserProfileInfoDto {
    private Long id;
    String username;
    String password;
    String role;
    boolean status;
    Long createAt;
    Long updateAt;
    Long deleteAt;
    int gold;
    int sliver;
}
