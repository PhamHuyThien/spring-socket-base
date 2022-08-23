package com.thiendz.example.springsocket.services.ws.chat;

import com.thiendz.example.springsocket.dto.enums.WsCommand;
import com.thiendz.example.springsocket.dto.ws.WsMessage;
import com.thiendz.example.springsocket.dto.ws.app.RoomInfo;
import com.thiendz.example.springsocket.dto.ws.req.CreateRoomReq;
import com.thiendz.example.springsocket.dto.ws.res.CreateRoomRes;
import com.thiendz.example.springsocket.model.UserProfile;
import com.thiendz.example.springsocket.utils.NumberUtil;
import com.thiendz.example.springsocket.websocket.WsChatApplication;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WsChatService {
    public static Map<String, RoomInfo> roomInfo = new ConcurrentHashMap<>();

    public static WsMessage<CreateRoomRes> createRoom(Session session, CreateRoomReq createRoomReq) {
        if (createRoomReq == null)
            return WsMessage.error(WsCommand.CHAT_CREATE_ROOM, -1, "Lỗi dữ liệu", CreateRoomRes.class);

        UserProfile master = WsChatApplication.sessions.get(session).getUserProfile();

        RoomInfo roomInfo = new RoomInfo();

        String roomId = NumberUtil.createIdRoom();
        roomInfo.setId(roomId);

        roomInfo.setName(createRoomReq.getName());
        roomInfo.setPassword(createRoomReq.getPassword());
        roomInfo.setMaster(master);
        roomInfo.setLimit(createRoomReq.getLimit());
        roomInfo.setMember(new ArrayList<>());
        roomInfo.setFee(createRoomReq.getFee());

        WsChatService.roomInfo.put(roomId, roomInfo);
        return WsMessage.success(WsCommand.CHAT_CREATE_ROOM, 1, "Tạo phòng thành công", new CreateRoomRes(roomId, roomInfo.getName()));
    }
}


