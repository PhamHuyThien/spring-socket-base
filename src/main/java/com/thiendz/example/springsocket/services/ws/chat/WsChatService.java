package com.thiendz.example.springsocket.services.ws.chat;

import com.thiendz.example.springsocket.dto.enums.WsCommand;
import com.thiendz.example.springsocket.dto.ws.UserSession;
import com.thiendz.example.springsocket.dto.ws.WsMessage;
import com.thiendz.example.springsocket.dto.ws.app.RoomInfo;
import com.thiendz.example.springsocket.dto.ws.req.CreateRoomReq;
import com.thiendz.example.springsocket.dto.ws.req.JoinRoomReq;
import com.thiendz.example.springsocket.dto.ws.req.OutRoomReq;
import com.thiendz.example.springsocket.dto.ws.req.RoomInfoReq;
import com.thiendz.example.springsocket.dto.ws.res.CreateRoomRes;
import com.thiendz.example.springsocket.dto.ws.res.JoinRoomRes;
import com.thiendz.example.springsocket.dto.ws.res.OutRoomRes;
import com.thiendz.example.springsocket.dto.ws.res.RoomInfoRes;
import com.thiendz.example.springsocket.utils.NumberUtil;

import javax.websocket.Session;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WsChatService {
    public static Map<String, RoomInfo> roomInfo = new ConcurrentHashMap<>();
    public static Map<Session, RoomInfo> roomJoin = new ConcurrentHashMap<>();

    public static WsMessage<CreateRoomRes> createRoom(UserSession<?> userSession, CreateRoomReq createRoomReq) {
        if (createRoomReq == null)
            return WsMessage.error(WsCommand.CHAT_CREATE_ROOM, -1, "Lỗi dữ liệu", CreateRoomRes.class);

        RoomInfo roomInfoJoin = WsChatService.roomJoin.get(userSession.getSession());
        if (roomInfoJoin != null)
            return WsMessage.error(WsCommand.CHAT_CREATE_ROOM, -2, "Bạn phải rời phòng mới có thể tạo phòng khác", CreateRoomRes.class);

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
        WsChatService.roomJoin.put(userSession.getSession(), roomInfo);
        
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

        if (roomInfo.getPassword() != null && !roomInfo.getPassword().equals(joinRoomReq.getPassword()))
            return WsMessage.error(WsCommand.CHAT_JOIN_ROOM, -4, "Mật khẩu phòng không chính xác", JoinRoomRes.class);

        RoomInfo roomInfoJoin = WsChatService.roomJoin.get(userSession.getSession());
        if (roomInfoJoin != null && !roomInfoJoin.getId().equals(joinRoomReq.getRoomId()))
            return WsMessage.error(WsCommand.CHAT_JOIN_ROOM, -5, "Bạn đang tham gia 1 phòng khác", JoinRoomRes.class);

        boolean isJoinRoom = roomInfo.getMembers()
                .stream()
                .anyMatch(u -> u.getSession().equals(userSession.getSession()));
        if (isJoinRoom)
            return WsMessage.error(WsCommand.CHAT_JOIN_ROOM, -6, "Bạn đã tham gia phòng này rồi", JoinRoomRes.class);

        WsChatService.roomInfo.get(joinRoomReq.getRoomId()).getMembers().add(userSession);
        WsChatService.roomJoin.put(userSession.getSession(), roomInfo);

        return WsMessage.success(WsCommand.CHAT_JOIN_ROOM, 1, "Tham gia thành công", new JoinRoomRes(WsChatService.roomInfo.get(joinRoomReq.getRoomId())));
    }

    public static WsMessage<OutRoomRes> outRoom(UserSession<?> userSession, OutRoomReq outRoomReq) {
        if (outRoomReq == null)
            return WsMessage.error(WsCommand.CHAT_OUT_ROOM, -1, "Lỗi dữ liệu", OutRoomRes.class);

        RoomInfo roomInfo = WsChatService.roomInfo.get(outRoomReq.getRoomId());
        if (roomInfo == null)
            return WsMessage.error(WsCommand.CHAT_OUT_ROOM, -2, "Room không tồn tại", OutRoomRes.class);

        Optional<UserSession<?>> optionalUserProfile = roomInfo
                .getMembers()
                .stream()
                .filter(u -> u.getSession().equals(userSession.getSession()))
                .findFirst();
        if (!optionalUserProfile.isPresent())
            return WsMessage.error(WsCommand.CHAT_OUT_ROOM, -3, "Bạn không có trong phòng này", OutRoomRes.class);

        WsChatService.roomInfo.get(outRoomReq.getRoomId()).getMembers().remove(optionalUserProfile.get());
        WsChatService.roomJoin.remove(userSession.getSession());

        return WsMessage.success(WsCommand.CHAT_OUT_ROOM, 1, "Rời phòng thành công", new OutRoomRes(WsChatService.roomInfo.get(outRoomReq.getRoomId())));
    }


}


