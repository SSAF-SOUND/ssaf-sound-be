package com.ssafy.ssafsound.domain.recruit.exception;

import lombok.Getter;

@Getter
public enum RecruitErrorInfo {

    INVALID_CHANGE_MEMBER_OPERATION("901","유효하지 않은 사용자 정보입니다."),
    INVALID_CHANGE_COMMENT_GROUP_OPERATION("902","유효하지 않은 리크루트 QNA 입니다."),
    INVALID_CHANGE_RECRUIT_OPERATION("903","유효하지 스터티/프로젝트 모집글입니다."),
    NOT_AUTHORIZATION_MEMBER("904","삭제 권한이 없는 사용자입니다."),
    NOT_RECRUITING_TYPE("905", "해당 스터디/프로젝트에서 모집하지 않는 역할군입니다."),
    NOT_SAME_LENGTH_RECRUIT_QUESTION_ANSWER("906", "질문에 대한 모든 답변이 필요합니다."),
    IS_ALREADY_FULL("907", "이미 모집 인원이 가득찬 스터디/프로젝트 입니다.");

    private final String code;
    private final String message;

    RecruitErrorInfo(String code, String message){
        this.code = code;
        this.message = message;
    }
}
