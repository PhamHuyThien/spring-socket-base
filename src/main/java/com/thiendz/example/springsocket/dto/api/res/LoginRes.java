package com.thiendz.example.springsocket.dto.api.res;

import com.thiendz.example.springsocket.dto.dto.UserProfileInfoDto;
import com.thiendz.example.springsocket.model.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRes {
    String token;
    UserProfileInfoDto userProfile;
}
