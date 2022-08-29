package com.thiendz.example.springsocket.controllers.ws;

import com.thiendz.example.springsocket.dto.LimitRequest;
import com.thiendz.example.springsocket.dto.enums.WsCommand;
import com.thiendz.example.springsocket.dto.ws.UserSession;
import com.thiendz.example.springsocket.dto.ws.WsMessage;
import com.thiendz.example.springsocket.dto.ws.app.ChatSession;
import com.thiendz.example.springsocket.dto.ws.app.RoomInfo;
import com.thiendz.example.springsocket.dto.ws.req.*;
import com.thiendz.example.springsocket.dto.ws.res.ChatRes;
import com.thiendz.example.springsocket.dto.ws.res.JoinRoomRes;
import com.thiendz.example.springsocket.dto.ws.res.OutRoomRes;
import com.thiendz.example.springsocket.services.ws.chat.WsChatService;
import com.thiendz.example.springsocket.utils.BeanUtil;
import com.thiendz.example.springsocket.utils.WsUtils;
import com.thiendz.example.springsocket.websocket.WsChatApplication;

import javax.websocket.Session;

public class WsChatController {
    public static void router(Session session, WsMessage<?> message) {
        LimitRequest limitRequest = BeanUtil.getApplicationContext().getBean(LimitRequest.class);
        UserSession<ChatSession> userSession = WsChatApplication.sessions.get(session);

        WsMessage<Void> limitReqPassMessage = limitRequest.isPass(message.getCmd(), session.getId());
        if (limitReqPassMessage.getCode() < 0) {
            WsUtils.sendMessage(session, limitReqPassMessage);
            return;
        }

        switch (message.getCmd()) {
            //{"cmd": "CHAT_CREATE_ROOM", "data": {"name": "name1", "limit": 100, "password": null, "fee": 0}}
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
            //{"cmd": "CHAT_JOIN_ROOM", "data": {"roomId": "123456", "password": null}}
            case CHAT_JOIN_ROOM:
                WsMessage<JoinRoomRes> joinRoomResWsMessage = WsChatService.joinRoom(userSession, message.dataCashTo(JoinRoomReq.class));
                if (!joinRoomResWsMessage.isError()) {
                    String username = userSession.getUserProfile().getUsername();
                    String strMessage = username + " đã tham gia nhóm";
                    WsMessage<Void> wsMessage = WsMessage.success(WsCommand.CHAT_JOIN_ROOM, 2, strMessage);
                    RoomInfo roomInfo = joinRoomResWsMessage.getData().getRoomInfo();
                    WsChatService.sendOldMessageToNewMember(userSession, roomInfo.getId());
                    WsChatService.roomChatHistoryAdd(roomInfo.getId(), joinRoomResWsMessage);
                    roomInfo.getMembers().forEach(us -> WsUtils.sendMessage(us.getSession(), wsMessage));
                }
                joinRoomResWsMessage.setData(null);
                WsUtils.sendMessage(session, joinRoomResWsMessage);
                break;
            //{"cmd": "CHAT_OUT_ROOM", "data": {"roomId": "123456"}}
            case CHAT_OUT_ROOM:
                WsMessage<OutRoomRes> outRoomResWsMessage = WsChatService.outRoom(userSession, message.dataCashTo(OutRoomReq.class));
                if (!outRoomResWsMessage.isError()) {
                    String username = userSession.getUserProfile().getUsername();
                    String strMessage = username + " đã rời nhóm";
                    WsMessage<Void> wsMessage = WsMessage.success(WsCommand.CHAT_OUT_ROOM, 2, strMessage);
                    RoomInfo roomInfo = outRoomResWsMessage.getData().getRoomInfo();
                    roomInfo.getMembers().forEach(us -> WsUtils.sendMessage(us.getSession(), wsMessage));
                    WsChatService.roomChatHistoryAdd(roomInfo.getId(), outRoomResWsMessage);
                }
                outRoomResWsMessage.setData(null);
                WsUtils.sendMessage(session, outRoomResWsMessage);
                break;
            //{"cmd": "CHAT", "data": {"id": "123456", "message": "ahihi do ngok"}}
            case CHAT:
                WsMessage<ChatRes> chatResWsMessage = WsChatService.chat(userSession, message.dataCashTo(ChatReq.class));
                if (!chatResWsMessage.isError()) {
                    RoomInfo roomInfo = chatResWsMessage.getData().getRoomInfo();
                    chatResWsMessage.getData().setRoomInfo(null);
                    chatResWsMessage.setCode(2);
                    roomInfo.getMembers().forEach(us -> WsUtils.sendMessage(us.getSession(), chatResWsMessage));
                    WsChatService.roomChatHistoryAdd(roomInfo.getId(), chatResWsMessage);
                }
                if (chatResWsMessage.getData() != null) {
                    chatResWsMessage.getData().setRoomInfo(null);
                    chatResWsMessage.getData().setUser(null);
                    chatResWsMessage.getData().setMessage(null);
                }
                chatResWsMessage.setCode(1);
                WsUtils.sendMessage(session, chatResWsMessage);
                break;
            default:
                WsUtils.sendMessage(session, message);
        }
    }
}
