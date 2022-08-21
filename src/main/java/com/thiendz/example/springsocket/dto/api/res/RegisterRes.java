package com.thiendz.example.springsocket.dto.api.res;

import com.thiendz.example.springsocket.model.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterRes {
    UserProfile profile;
}
