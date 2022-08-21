package com.thiendz.example.springsocket.services;

import com.thiendz.example.springsocket.dto.api.res.WsUserConnect;
import com.thiendz.example.springsocket.websocket.WsChatApplication;
import com.thiendz.example.springsocket.websocket.WsPortalApplication;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    public WsUserConnect getChatUserConnect(){
        return new WsUserConnect(WsChatApplication.sessions.size());
    }
    public WsUserConnect getPortalUserConnect(){
        return new WsUserConnect(WsPortalApplication.sessions.size());
    }

}
