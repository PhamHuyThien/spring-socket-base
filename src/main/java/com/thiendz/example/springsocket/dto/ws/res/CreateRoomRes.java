package com.thiendz.example.springsocket.dto.ws.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateRoomRes {
    String id;
    String name;
}