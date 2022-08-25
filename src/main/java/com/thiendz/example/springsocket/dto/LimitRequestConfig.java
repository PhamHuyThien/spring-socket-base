package com.thiendz.example.springsocket.dto;

import com.thiendz.example.springsocket.dto.enums.WsCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LimitRequestConfig {
    WsCommand cmd;
    int rate;
    int code;
    String message;
}
