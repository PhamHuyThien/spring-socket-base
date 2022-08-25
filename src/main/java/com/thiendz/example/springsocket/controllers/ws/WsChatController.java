package com.thiendz.example.springsocket.controllers.ws;

import com.thiendz.example.springsocket.dto.enums.WsCommand;
import com.thiendz.example.springsocket.dto.ws.UserSession;
import com.thiendz.example.springsocket.dto.ws.WsMessage;
import com.thiendz.example.springsocket.dto.ws.app.ChatSession;
import com.thiendz.example.springsocket.dto.ws.req.CreateRoomReq;
import com.thiendz.example.springsocket.dto.ws.req.JoinRoomReq;
import com.thiendz.example.springsocket.dto.ws.req.RoomInfoReq;
import com.thiendz.example.springsocket.dto.ws.res.JoinRoomRes;
import com.thiendz.example.springsocket.services.ws.chat.WsChatService;
import com.thiendz.example.springsocket.utils.WsUtils;
import com.thiendz.example.springsocket.websocket.WsChatApplication;

import javax.websocket.Session;

public class WsChatController {
    public static void router(Session session, WsMessage<?> message) {
        UserSession<ChatSession> userSession = WsChatApplication.sessions.get(session);
        switch (message.getCmd()) {
            //{"cmd": "CHAT_CREATE_ROOM", "data": {"name": "name1", "limit": 100, "password": "", "fee": 0}}
            case CHAT_CREATE_ROOM:
                WsUtils.sendMessage(session, WsChatService.createRoom(userSession, message.dataCashTo(CreateRoomReq.class)));
                break;
            //{"cmd": "CHAT_ROOM_INFO", "data": {"roomId": "123456"}}
            case CHAT_ROOM_INFO:
                WsUtils.sendMessage(session, WsChatService.roomInfo(message.dataCashTo(RoomInfoReq.class)));
                break;
            //{"cmd": "CHAT_LIST_ROOM_INFO"}
            case CHAT_LIST_ROOM_INFO:
                WsUtils.sendMessage(session, WsChatService.roomInfo());
                break;
            //{"cmd": "CHAT_JOIN_ROOM", "data": {"roomId": "123456"}}
            case CHAT_JOIN_ROOM:
                WsMessage<JoinRoomRes> joinRoomResWsMessage = WsChatService.joinRoom(userSession, message.dataCashTo(JoinRoomReq.class));
                if (joinRoomResWsMessage.isError()) {
                    WsUtils.sendMessage(session, joinRoomResWsMessage);
                    break;
                }
                String username = userSession.getUserProfile().getUsername();
                String strMessage = username + " đã tham gia nhóm";
                WsMessage<String> wsMessage = WsMessage.success(WsCommand.CHAT_CREATE_ROOM, 2, strMessage);
                joinRoomResWsMessage
                        .getData()
                        .getRoomInfo()
                        .getMembers()
                        .forEach(us -> WsUtils.sendMessage(us.getSession(), wsMessage));
                break;
            default:
                WsUtils.sendMessage(session, message);
        }
    }
}
