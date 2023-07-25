package com.ssafy.ssafsound.domain.chat.exception;

import lombok.Getter;

@Getter
public enum ChatErrorInfo {

    INVALID_CHAT_ROOM_ID("1001","유효하지 않은 데이터입니다."),
    UNAUTHORIZED_MEMBER("1002","삭제 권한이 없는 사용자입니다."),
    INACTIVE_CHAT_ROOM("1003","채팅이 불가능한 채팅방입니다.");

    private String code;
    private String message;

    ChatErrorInfo(String code, String message){
        this.code = code;
        this.message = message;
    }
}
