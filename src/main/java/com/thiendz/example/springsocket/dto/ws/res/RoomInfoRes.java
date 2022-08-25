package com.thiendz.example.springsocket.dto.ws.res;

import com.thiendz.example.springsocket.dto.ws.app.RoomInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoomInfoRes {
    RoomInfo roomInfo;
    int totalUser;
}
