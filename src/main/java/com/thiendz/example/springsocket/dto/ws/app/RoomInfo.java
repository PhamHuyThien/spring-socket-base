package com.thiendz.example.springsocket.dto.ws.app;

import com.thiendz.example.springsocket.model.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoomInfo {
    String id;
    String name;
    int limit;
    String password;
    int fee;
    UserProfile master;
    List<UserProfile> member;
}
