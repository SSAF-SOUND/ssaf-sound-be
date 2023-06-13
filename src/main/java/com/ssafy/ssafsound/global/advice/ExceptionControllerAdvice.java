package com.ssafy.ssafsound.global.advice;

import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    public EnvelopeResponse RuntimeExceptionHandler(RuntimeException e) {
        log.error(e.getMessage());
        return EnvelopeResponse.builder()
                .code(GlobalErrorInfo.INTERNAL_SERVER_ERROR.getCode())
                .message(GlobalErrorInfo.INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }
}