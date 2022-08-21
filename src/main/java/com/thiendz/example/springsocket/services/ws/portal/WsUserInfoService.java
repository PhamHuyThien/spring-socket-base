package com.thiendz.example.springsocket.services.ws.portal;

import com.thiendz.example.springsocket.dto.enums.WsCommand;
import com.thiendz.example.springsocket.dto.ws.UserSession;
import com.thiendz.example.springsocket.dto.ws.WsMessage;
import com.thiendz.example.springsocket.dto.ws.app.PortalSession;
import com.thiendz.example.springsocket.model.UserProfile;
import com.thiendz.example.springsocket.websocket.WsPortalApplication;

import javax.websocket.Session;

public class WsUserInfoService {
    public static WsMessage<UserProfile> getUserInfo(Session session) {
        UserSession<PortalSession> userSession = WsPortalApplication.sessions.get(session);
        if (userSession == null)
            return WsMessage.error(WsCommand.USER_PROFILE_INFO, -10, "tài khoản không hợp lệ", UserProfile.class);
        return WsMessage.success(WsCommand.USER_PROFILE_INFO, 1, "Thành công", userSession.getUserProfile());
    }
}
