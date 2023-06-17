package com.ssafy.ssafsound.domain.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthException extends RuntimeException {
    private MemberErrorInfo info;
}
