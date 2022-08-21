package com.thiendz.example.springsocket.controllers.ws;

import com.thiendz.example.springsocket.dto.ws.WsMessage;
import com.thiendz.example.springsocket.utils.WsUtils;

import javax.websocket.Session;

public class WsChatController {
    public static void router(Session session, WsMessage<?> message) {
        switch (message.getCmd()) {
            default:
                WsUtils.sendMessage(session, message);
        }
    }
}
