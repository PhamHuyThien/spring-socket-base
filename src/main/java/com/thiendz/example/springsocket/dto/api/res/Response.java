package com.thiendz.example.springsocket.dto.api.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Response<T> {
    boolean error;
    int code;
    String message;
    T data;

    public static Response<Void> error(int code, String message) {
        return new Response<>(true, code, message, null);
    }

    public static <T> Response<T> success(int code, String message, T data) {
        return new Response<>(false, code, message, data);
    }
}
