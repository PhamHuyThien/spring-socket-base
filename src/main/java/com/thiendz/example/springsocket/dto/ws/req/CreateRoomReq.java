package com.thiendz.example.springsocket.dto.ws.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateRoomReq {
    String name;
    int limit;
    String password;
    int fee;
}
