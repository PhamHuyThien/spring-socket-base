package com.thiendz.example.springsocket.websocket;

import com.thiendz.example.springsocket.controllers.ws.WsChatController;
import com.thiendz.example.springsocket.dto.enums.WsCommand;
import com.thiendz.example.springsocket.dto.ws.UserSession;
import com.thiendz.example.springsocket.dto.ws.WsMessage;
import com.thiendz.example.springsocket.dto.ws.app.ChatSession;
import com.thiendz.example.springsocket.utils.WsUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket/chat")
public class WsChatApplication {
    public static Map<Session, UserSession<ChatSession>> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        UserSession<ChatSession> userSession = WsUtils.onConnectParser(session);
        if (userSession == null) {
            WsUtils.sendMessage(session, WsMessage.error(WsCommand.AUTH_LOGIN, -10, "Token sai hoặc hết hạn"));
            WsUtils.closeSession(session);
            return;
        }
        WsChatApplication.sessions.put(session, userSession);
        WsUtils.sendMessage(session, WsMessage.success(WsCommand.AUTH_LOGIN, 1, "đăng nhập thành công", userSession.getUserProfile()));
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        WsChatController.router(session, WsUtils.onMessageParser(message));
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        sessions.remove(session);
        throwable.printStackTrace();
    }
}
