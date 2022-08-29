package com.thiendz.example.springsocket.dto.enums;

public enum WsCommand {
    __,
    //AUTH
    AUTH_LOGIN,
    //USER_PROFILE
    USER_PROFILE_INFO,

    //ERROR
    ERROR_COMMAND,
    ERROR_ONE_PER_USER,
    //NOT_FOUND
    NOT_FOUND_COMMAND,

    //CHAT
    CHAT_CREATE_ROOM,
    CHAT_ROOM_INFO,
    CHAT_LIST_ROOM_INFO,
    CHAT_JOIN_ROOM,
    CHAT_OUT_ROOM,
    CHAT,
}
