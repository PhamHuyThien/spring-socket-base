package com.thiendz.example.springsocket.configs;

import com.thiendz.example.springsocket.websocket.WsChatApplication;
import com.thiendz.example.springsocket.websocket.WsPortalApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    }

    @Bean
    public WsPortalApplication wsPortalApplication() {
        return new WsPortalApplication();
    }

    @Bean
    public WsChatApplication wsChatApplication() {
        return new WsChatApplication();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
