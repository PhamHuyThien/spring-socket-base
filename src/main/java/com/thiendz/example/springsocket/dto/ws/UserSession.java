package com.thiendz.example.springsocket.dto.ws;

import com.thiendz.example.springsocket.model.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserSession<T> {
    UserProfile userProfile;
    T data;
}
