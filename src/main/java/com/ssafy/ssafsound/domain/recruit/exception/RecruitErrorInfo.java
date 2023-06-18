package com.ssafy.ssafsound.domain.recruit.exception;

import lombok.Getter;

@Getter
public enum RecruitErrorInfo {

    INVALID_CHANGE_MEMBER_OPERATION("901","유효하지 않은 사용자 정보입니다."),
    INVALID_CHANGE_COMMENT_GROUP_OPERATION("902","유효하지 않은 리크루트 QNA 입니다."),
    INVALID_CHANGE_RECRUIT_OPERATION("903","유효하지 스터티/프로젝트 모집글입니다."),
    NOT_AUTHORIZATION_MEMBER("904","삭제 권한이 없는 사용자입니다.");

    private final String code;
    private final String message;

    RecruitErrorInfo(String code, String message){
        this.code = code;
        this.message = message;
    }
}
