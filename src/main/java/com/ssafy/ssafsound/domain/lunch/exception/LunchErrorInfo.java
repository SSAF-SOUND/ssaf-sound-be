package com.ssafy.ssafsound.domain.lunch.exception;

import lombok.Getter;

@Getter
public enum LunchErrorInfo {
    INVALID_DATE("601","유효하지 않은 날짜입니다."),
    INVALID_LUNCH_ID("602","유효하지 않은 데이터입니다."),
    DUPLICATE_LUNCH_POLL("604","이미 투표한 메뉴입니다."),
    NO_LUNCH_POLL("605","투표한 적이 없는 메뉴입니다."),
    SCRAPING_ERROR("606","메뉴 스크래핑 도중 에러가 발생했습니다.");

    private String code;
    private String message;

    LunchErrorInfo(String code, String message){
        this.code = code;
        this.message = message;
    }
}
