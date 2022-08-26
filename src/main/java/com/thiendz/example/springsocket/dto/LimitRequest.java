package com.thiendz.example.springsocket.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiendz.example.springsocket.dto.enums.WsCommand;
import com.thiendz.example.springsocket.dto.ws.UserSession;
import com.thiendz.example.springsocket.dto.ws.WsMessage;
import com.thiendz.example.springsocket.utils.BeanUtil;
import com.thiendz.example.springsocket.utils.FileUtils;

import javax.websocket.Session;
import java.io.IOException;
import java.util.*;

public class LimitRequest {
    private final Map<WsCommand, TimeLimit> commandTimeLimit = new HashMap<>();
    private final Map<String, Long> userLimitRequest = new HashMap<>();

    public LimitRequest loadConfig() {
        ObjectMapper objectMapper = BeanUtil.getApplicationContext().getBean(ObjectMapper.class);
        try {
            String data = FileUtils.getLimitRequestResource("limit_req");
            LimitRequestConfig[] limitRequestConfigs = objectMapper.readValue(data, LimitRequestConfig[].class);
            for (LimitRequestConfig limitRequestConfig : limitRequestConfigs) {
                WsCommand wsCommand = limitRequestConfig.getCmd();
                TimeLimit timeLimit = new TimeLimit();
                timeLimit.setSpeedBy((int) (1000 / limitRequestConfig.getRate()));
                timeLimit.setCode(limitRequestConfig.getCode());
                timeLimit.setMessage(limitRequestConfig.getMessage());
                this.commandTimeLimit.put(wsCommand, timeLimit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public WsMessage<Void> isPass(WsCommand wsCommand, String key) {
        TimeLimit timeLimit = commandTimeLimit.get(wsCommand);
        TimeLimit timeLimitAll = commandTimeLimit.get(WsCommand.__);
        TimeLimit timeLimitUsing = null;

        WsMessage<Void> messagePassed = WsMessage.success(wsCommand, 1, "request passed");

        if (timeLimit != null)
            timeLimitUsing = timeLimit;
        else if (timeLimitAll != null)
            timeLimitUsing = timeLimitAll;

        if (timeLimitUsing == null)
            return messagePassed;

        long currentTime = new Date().getTime();

        Long userLimitReq = userLimitRequest.get(key);
        if (userLimitReq == null)
            userLimitReq = 0L;

        if (currentTime - userLimitReq < timeLimitUsing.getSpeedBy())
            return WsMessage.error(wsCommand, timeLimitUsing.getCode(), timeLimitUsing.getMessage(), Void.class);

        userLimitRequest.put(key, currentTime);

        return messagePassed;
    }
}
