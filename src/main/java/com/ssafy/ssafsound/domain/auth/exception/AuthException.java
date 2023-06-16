package com.ssafy.ssafsound.domain.auth.exception;

import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthException extends RuntimeException {
    private GlobalErrorInfo info;
}
