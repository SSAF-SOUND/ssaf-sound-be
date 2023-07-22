package com.ssafy.ssafsound.domain.chat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatException extends RuntimeException {

    private ChatErrorInfo info;
}
