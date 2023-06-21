package com.ssafy.ssafsound.domain.member.exception;

import com.ssafy.ssafsound.domain.auth.exception.AuthErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberException extends RuntimeException {
    private MemberErrorInfo info;
}
