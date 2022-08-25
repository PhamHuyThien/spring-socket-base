package com.thiendz.example.springsocket.services.ws.chat;

import com.thiendz.example.springsocket.dto.enums.WsCommand;
import com.thiendz.example.springsocket.dto.ws.UserSession;
import com.thiendz.example.springsocket.dto.ws.WsMessage;
import com.thiendz.example.springsocket.dto.ws.app.RoomInfo;
import com.thiendz.example.springsocket.dto.ws.req.CreateRoomReq;
import com.thiendz.example.springsocket.dto.ws.req.JoinRoomReq;
import com.thiendz.example.springsocket.dto.ws.req.RoomInfoReq;
import com.thiendz.example.springsocket.dto.ws.res.CreateRoomRes;
import com.thiendz.example.springsocket.dto.ws.res.JoinRoomRes;
import com.thiendz.example.springsocket.dto.ws.res.RoomInfoRes;
import com.thiendz.example.springsocket.utils.NumberUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WsChatService {
    public static Map<String, RoomInfo> roomInfo = new ConcurrentHashMap<>();

    public static WsMessage<CreateRoomRes> createRoom(UserSession<?> userSession, CreateRoomReq createRoomReq) {
        if (createRoomReq == null)
            return WsMessage.error(WsCommand.CHAT_CREATE_ROOM, -1, "Lỗi dữ liệu", CreateRoomRes.class);

        RoomInfo roomInfo = new RoomInfo();

        String roomId = NumberUtil.createIdRoom();
        roomInfo.setId(roomId);

        roomInfo.setName(createRoomReq.getName());
        roomInfo.setPassword(createRoomReq.getPassword());
        roomInfo.setMaster(userSession);
        roomInfo.setLimit(createRoomReq.getLimit());

        List<UserSession<?>> sessionMem = new ArrayList<>();
        sessionMem.add(userSession);
        roomInfo.setMembers(sessionMem);
        roomInfo.setFee(createRoomReq.getFee());

        WsChatService.roomInfo.put(roomId, roomInfo);
        return WsMessage.success(WsCommand.CHAT_CREATE_ROOM, 1, "Tạo phòng thành công", new CreateRoomRes(roomId, roomInfo.getName()));
    }

    public static WsMessage<RoomInfoRes> roomInfo(RoomInfoReq roomInfoReq) {
        if (roomInfoReq == null)
            return WsMessage.error(WsCommand.CHAT_ROOM_INFO, -1, "Lỗi dữ liệu", RoomInfoRes.class);
        RoomInfo roomInfo = WsChatService.roomInfo.get(roomInfoReq.getRoomId());
        if (roomInfo == null)
            return WsMessage.error(WsCommand.CHAT_ROOM_INFO, -2, "Room không tồn tại", RoomInfoRes.class);
        RoomInfoRes roomInfoRes = new RoomInfoRes();
        roomInfoRes.setRoomInfo(roomInfo);
        roomInfoRes.setTotalUser(roomInfo.getMembers().size());
        return WsMessage.success(WsCommand.CHAT_ROOM_INFO, 1, "Thành công", roomInfoRes);
    }

    public static WsMessage<List<RoomInfoRes>> roomInfo() {
        List<RoomInfoRes> roomInfoResList = roomInfo
                .values()
                .stream()
                .map(roomInfo1 -> new RoomInfoRes(roomInfo1, roomInfo1.getMembers().size()))
                .collect(Collectors.toList());
        return WsMessage.success(WsCommand.CHAT_LIST_ROOM_INFO, 1, "Thành công", roomInfoResList);
    }

    public static WsMessage<JoinRoomRes> joinRoom(UserSession<?> userSession, JoinRoomReq joinRoomReq) {
        if (joinRoomReq == null)
            return WsMessage.error(WsCommand.CHAT_JOIN_ROOM, -1, "Lỗi dữ liệu", JoinRoomRes.class);

        RoomInfo roomInfo = WsChatService.roomInfo.get(joinRoomReq.getRoomId());
        if (roomInfo == null)
            return WsMessage.error(WsCommand.CHAT_JOIN_ROOM, -2, "Room không tồn tại", JoinRoomRes.class);

        if (roomInfo.getMembers().size() >= roomInfo.getLimit())
            return WsMessage.error(WsCommand.CHAT_JOIN_ROOM, -3, "Phòng đã đủ người", JoinRoomRes.class);

        if (Objects.equals(roomInfo.getMaster().getSession(), userSession.getSession()))
            return WsMessage.error(WsCommand.CHAT_JOIN_ROOM, -4, "Bạn là chủ phòng, khong thể tham gia", JoinRoomRes.class);

        boolean isJoinRoom = roomInfo.getMembers()
                .stream()
                .anyMatch(u -> u.getSession().equals(userSession.getSession()));
        if (isJoinRoom)
            return WsMessage.error(WsCommand.CHAT_JOIN_ROOM, -5, "Bạn đã tham gia phòng này rồi", JoinRoomRes.class);

        WsChatService.roomInfo.get(joinRoomReq.getRoomId()).getMembers().add(userSession);
        return WsMessage.success(WsCommand.CHAT_JOIN_ROOM, 1, "Tham gia thành công", new JoinRoomRes(WsChatService.roomInfo.get(joinRoomReq.getRoomId())));
    }
}


