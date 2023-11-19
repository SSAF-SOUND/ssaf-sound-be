package com.ssafy.ssafsound.domain.notification.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationException extends RuntimeException{
    private NotificationErrorInfo info;
}
