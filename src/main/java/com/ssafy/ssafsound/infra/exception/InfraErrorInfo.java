package com.ssafy.ssafsound.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InfraErrorInfo {
    STORAGE_SERVICE_ERROR("1101","스토리지 서비스 에러가 발생했습니다."),
    STORAGE_DELETE_UNAUTHORIZED("1102","권한이 없는 삭제 요청입니다."),
    MESSAGING_SERVICE_ERROR("1103","메세징 서비스 에러가 발생했습니다.");

    private String code;
    private String message;
}
