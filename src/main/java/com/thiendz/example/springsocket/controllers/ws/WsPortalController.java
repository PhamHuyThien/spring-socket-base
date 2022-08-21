package com.thiendz.example.springsocket.controllers.ws;

import com.thiendz.example.springsocket.dto.ws.WsMessage;
import com.thiendz.example.springsocket.services.ws.portal.WsUserInfoService;
import com.thiendz.example.springsocket.utils.WsUtils;

import javax.websocket.Session;

public class WsPortalController {
    public static void router(Session session, WsMessage<?> message) {
        switch (message.getCmd()) {
            case USER_PROFILE_INFO:
                WsUtils.sendMessage(session, WsUserInfoService.getUserInfo(session));
                break;
            default:
                WsUtils.sendMessage(session, message);
        }
    }
}
