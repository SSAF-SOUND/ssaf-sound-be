package com.ssafy.ssafsound.domain.lunch.exception;

import lombok.Getter;

@Getter
public enum LunchErrorInfo {
    INVALID_DATE("601","유효하지 않은 날짜입니다."),
    INVALID_LUNCH_ID("602","유효하지 않은 점심 데이터입니다."),
    NO_LUNCH_DATE("603","점심 데이터가 존재하지 않는 날짜입니다."),
    DUPLICATE_LUNCH_POLL("604","이미 투표한 점심 메뉴입니다.");

    private String code;
    private String message;

    LunchErrorInfo(String code, String message){
        this.code = code;
        this.message = message;
    }
}
