package com.ssafy.ssafsound.domain.auth.exception;

import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthException extends RuntimeException {
    private GlobalErrorInfo info;
}
