package com.thiendz.example.springsocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TimeLimit {
    long firstTime;
    int speedBy;
    int code;
    String message;
}