package com.thiendz.example.springsocket.websocket;

import com.thiendz.example.springsocket.controllers.ws.WsChatController;
import com.thiendz.example.springsocket.dto.enums.WsCommand;
import com.thiendz.example.springsocket.dto.ws.UserSession;
import com.thiendz.example.springsocket.dto.ws.WsMessage;
import com.thiendz.example.springsocket.dto.ws.app.ChatSession;
import com.thiendz.example.springsocket.services.ws.chat.WsChatService;
import com.thiendz.example.springsocket.utils.WsUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket/chat")
public class WsChatApplication {
    public static Map<Session, UserSession<ChatSession>> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        //kiểm tra authen
        UserSession<ChatSession> userSession = WsUtils.onConnectParser(session);
        if (userSession == null) {
            WsUtils.sendMessage(session, WsMessage.error(WsCommand.AUTH_LOGIN, -10, "Token sai hoặc hết hạn"));
            WsUtils.closeSession(session);
            return;
        }
        // kiểm tra đăng nhập trên nhiều thiết bị
        List<UserSession<ChatSession>> userFilter = WsUtils.isOnePerConnect(sessions, userSession);
        if (userFilter.size() > 0) {
            for (UserSession<ChatSession> userSessionFilter : userFilter) {
                Session sessionFilter = userSessionFilter.getSession();
                WsUtils.sendMessage(sessionFilter, WsMessage.error(WsCommand.ERROR_ONE_PER_USER, -777, "Bạn đang đăng nhập trên 1 thiết bị khác"));
                WsUtils.closeSession(sessionFilter);
                WsChatApplication.sessions.remove(sessionFilter);
            }
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
        WsChatService.closeAllInChatService(WsChatApplication.sessions.get(session));
        WsChatApplication.sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        WsChatService.closeAllInChatService(WsChatApplication.sessions.get(session));
        WsChatApplication.sessions.remove(session);
        throwable.printStackTrace();
    }
}
