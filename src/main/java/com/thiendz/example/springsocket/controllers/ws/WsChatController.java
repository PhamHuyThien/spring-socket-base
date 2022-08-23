package com.thiendz.example.springsocket.controllers.ws;

import com.thiendz.example.springsocket.dto.ws.WsMessage;
import com.thiendz.example.springsocket.dto.ws.req.CreateRoomReq;
import com.thiendz.example.springsocket.services.ws.chat.WsChatService;
import com.thiendz.example.springsocket.utils.WsUtils;

import javax.websocket.Session;

public class WsChatController {
    public static void router(Session session, WsMessage<?> message) {
        switch (message.getCmd()) {
            //{"cmd": "CHAT_CREATE_ROOM", "data": {"name": "name1", "limit": 100, "password": "", "fee": 0}}
            case CHAT_CREATE_ROOM:
                WsUtils.sendMessage(session, WsChatService.createRoom(session, message.cashData(CreateRoomReq.class)));
                break;
            default:
                WsUtils.sendMessage(session, message);
        }
    }
}
