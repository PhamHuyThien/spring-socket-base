package com.thiendz.example.springsocket.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiendz.example.springsocket.auths.JwtTokenProvider;
import com.thiendz.example.springsocket.configs.SpringContext;
import com.thiendz.example.springsocket.dto.enums.WsCommand;
import com.thiendz.example.springsocket.dto.ws.UserSession;
import com.thiendz.example.springsocket.dto.ws.WsMessage;
import com.thiendz.example.springsocket.model.UserProfile;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class WsUtils {
    public static <T> UserSession<T> onConnectParser(Session session) {
        Map<String, String> queryParams = URLUtil.parseQueryParam(session.getQueryString());
        JwtTokenProvider jwtTokenProvider = SpringContext.getApplicationContext().getBean(JwtTokenProvider.class);
        String token = queryParams.get("token");
        UserProfile userProfile;
        try {
            userProfile = jwtTokenProvider.toUserProfile(token);
        } catch (Exception e) {
            return null;
        }
        UserSession<T> userSession = new UserSession<>();
        userSession.setUserProfile(userProfile);
        return userSession;
    }

    public static WsMessage<?> onMessageParser(String message) {
        ObjectMapper objectMapper = SpringContext.getApplicationContext().getBean(ObjectMapper.class);
        WsMessage<?> wsMessage = WsMessage.error(WsCommand.ERROR_COMMAND, -5, "Lỗi cú pháp", message);
        try {
            wsMessage = objectMapper.readValue(message, WsMessage.class);
        } catch (JsonProcessingException ignored) {
        }
        return wsMessage;
    }

    public static void sendMessage(Session to, WsMessage<?> message) {
        try {
            to.getBasicRemote().sendText(message.toStringJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Map<Session, ?> to, WsMessage<?> message) {
        to.forEach((session, o) -> {
            try {
                session.getBasicRemote().sendText(message.toStringJson());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static Session findSessionByUsername(Map<Session, UserSession<?>> sessions, String username){
        Optional<Session> sessionOptional = sessions
                .keySet()
                .stream()
                .filter(session -> sessions.get(session).getUserProfile().getUsername().equals(username))
                .findAny();
        return sessionOptional.orElse(null);
    }
}
