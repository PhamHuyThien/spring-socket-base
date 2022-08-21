package com.thiendz.example.springsocket.controllers;

import com.thiendz.example.springsocket.dto.api.res.Response;
import com.thiendz.example.springsocket.dto.api.res.WsUserConnect;
import com.thiendz.example.springsocket.services.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/socket")
public class SocketController {
    @Autowired
    WebSocketService webSocketService;

    @GetMapping("/chat/user-connect")
    public ResponseEntity<Response<WsUserConnect>> getChatUserConnect() {
        return ResponseEntity.ok(Response.success(1, "Thành công", webSocketService.getChatUserConnect()));
    }

    @GetMapping("/portal/user-connect")
    public ResponseEntity<Response<WsUserConnect>> getPortalUserConnect() {
        return ResponseEntity.ok(Response.success(1, "Thành công", webSocketService.getPortalUserConnect()));
    }
}
