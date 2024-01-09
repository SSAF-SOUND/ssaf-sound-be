package com.ssafy.ssafsound.domain.notification.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.notification.dto.*;
import com.ssafy.ssafsound.domain.notification.service.NotificationService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/cursor")
    public EnvelopeResponse<GetNotificationCursorResDto> getNotificationsByCursor(@Authentication AuthenticatedMember authenticatedMember,
                                                                                  @Valid @ModelAttribute GetNotificationCursorReqDto getNotificationCursorReqDto) {
        return EnvelopeResponse.<GetNotificationCursorResDto>builder()
                .data(notificationService.getNotificationsByCursor(authenticatedMember, getNotificationCursorReqDto))
                .build();
    }

    @GetMapping("/offset")
    public EnvelopeResponse<GetNotificationOffsetResDto> getNotificationsByOffset(@Authentication AuthenticatedMember authenticatedMember,
                                                                                  @Valid @ModelAttribute GetNotificationOffsetReqDto getNotificationOffsetReqDto) {

        return EnvelopeResponse.<GetNotificationOffsetResDto>builder()
                .data(notificationService.getNotificationsByOffset(authenticatedMember, getNotificationOffsetReqDto))
                .build();
    }

    @GetMapping("/new")
    public EnvelopeResponse<GetCheckNotificationResDto> checkNotification(@Authentication AuthenticatedMember authenticatedMember) {
        return EnvelopeResponse.<GetCheckNotificationResDto>builder()
                .data(notificationService.checkNotification(authenticatedMember))
                .build();
    }
}
