package com.ssafy.ssafsound.domain.notification.controller;

import com.ssafy.ssafsound.domain.notification.service.NotificationService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping()
    public EnvelopeResponse<Void> getNotificationsByMemberId() {
        log.info("사용자 알림 조회 컨트롤러");
        return EnvelopeResponse.<Void>builder()
                .data(notificationService.getNotifications())
                .build();
    }
}
