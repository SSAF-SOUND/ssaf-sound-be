package com.ssafy.ssafsound.domain.member.exception;

import lombok.Getter;

@Getter
public enum MemberErrorInfo {

    MEMBER_ROLE_TYPE_NOT_FOUND("703", "멤버 권한을 찾을 수 없는 문제가 발생했습니다."),
    MEMBER_NOT_FOUND_BY_ID("704", "멤버를 찾을 수 없습니다."),
    MEMBER_TOKEN_NOT_FOUND("708", "멤버 ID로 토큰을 찾을 수 없습니다."),
    MEMBER_OAUTH_NOT_FOUND("709", "소셜 로그인에 문제가 발생했습니다."),
    MEMBER_INFORMATION_ERROR("710", "멤버 정보 조회에서 문제가 발생했습니다."),
    MEMBER_NICKNAME_DUPLICATION("711", "중복되는 닉네임입니다."),
    MEMBER_CERTIFICATED_FAIL("712", "인증 시도 가능 횟수를 초과하여 일정 시간이 자나야 재시도 할 수 있습니다."),
    SEMESTER_NOT_FOUND("713", "SSAFY Member 등록 또는 기수 수정 시, 기수 값은 필수입니다."),
    MEMBER_PROFILE_SECRET("714", "멤버의 프로필 공개 여부를 확인하세요"),
    MEMBER_NOT_SSAFY("715", "싸피생이 아닙니다."),
    MEMBER_DELETED("716", "탈퇴한 회원입니다."),
    MEMBER_INPUT_INFORMATION_FAIL("717", "약관에 동의하셔야 합니다.");

    private final String code;
    private final String message;

    MemberErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
