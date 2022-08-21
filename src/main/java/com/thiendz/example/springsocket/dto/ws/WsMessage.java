package com.thiendz.example.springsocket.dto.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiendz.example.springsocket.configs.SpringContext;
import com.thiendz.example.springsocket.dto.enums.WsCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WsMessage<T> {
    WsCommand cmd;
    boolean error;
    int code;
    String message;
    T data;

    public static WsMessage<Void> error(WsCommand cmd, int code, String message) {
        return new WsMessage<>(cmd, true, code, message, null);
    }
    public static <T> WsMessage<T> error(WsCommand cmd, int code, String message, Class<T> clazz) {
        return new WsMessage<>(cmd, true, code, message, null);
    }
    public static <T> WsMessage<T> error(WsCommand cmd, int code, String message, T data) {
        return new WsMessage<>(cmd, true, code, message, data);
    }
    public static <T> WsMessage<T> success(WsCommand cmd, int code, String message) {
        return new WsMessage<T>(cmd, false, code, message, null);
    }
    public static <T> WsMessage<T> success(WsCommand cmd, int code, String message, T data) {
        return new WsMessage<T>(cmd, false, code, message, data);
    }
    public String toStringJson() {
        ObjectMapper objectMapper = SpringContext.getApplicationContext().getBean(ObjectMapper.class);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(this);
        } catch (Exception ignored) {
        }
        return json;
    }
}
