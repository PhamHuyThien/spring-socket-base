package com.thiendz.example.springsocket.dto.ws.res;

import com.thiendz.example.springsocket.dto.ws.app.RoomInfo;
import com.thiendz.example.springsocket.model.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatRes {
    String id;
    UserProfile user;
    String message;
    long createAt;
    RoomInfo roomInfo;
}
