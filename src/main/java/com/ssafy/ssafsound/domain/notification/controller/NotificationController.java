package com.ssafy.ssafsound.domain.notification.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.notification.dto.GetNotificationResDto;
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
    public EnvelopeResponse<GetNotificationResDto> getNotificationsByMemberId() {
        AuthenticatedMember authenticatedMember = AuthenticatedMember.builder()
                .memberId(1L)
                .memberRole("user")
                .build();

        return EnvelopeResponse.<GetNotificationResDto>builder()
                .data(notificationService.getNotifications(authenticatedMember))
                .build();
    }

//    @GetMapping("/save")
//    public EnvelopeResponse<Void> saveNotification(@RequestParam Long id) {
//        CreateNotification
//
//        return EnvelopeResponse.<Void>builder()
//                .data(notificationService.saveNotification(id))
//                .build();
//    }
}
