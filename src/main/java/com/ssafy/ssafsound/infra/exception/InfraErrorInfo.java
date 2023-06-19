package com.ssafy.ssafsound.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InfraErrorInfo {
    STORAGE_SERVICE_ERROR("1101","스토리지 서비스 에러가 발생했습니다."),
    STORAGE_STORE_INVALID_OBJECT("1102","유효하지 않은 객체 저장을 시도하였습니다."),
    MESSAGING_SERVICE_ERROR("1103","메세징 서비스 에러가 발생했습니다.");

    private String code;
    private String message;
}
