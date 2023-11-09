package com.ssafy.ssafsound.domain.notification.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.notification.domain.Notification;
import com.ssafy.ssafsound.domain.notification.dto.GetNotificationResDto;
import com.ssafy.ssafsound.domain.notification.event.NotificationEvent;
import com.ssafy.ssafsound.domain.notification.exception.NotificationErrorInfo;
import com.ssafy.ssafsound.domain.notification.exception.NotificationException;
import com.ssafy.ssafsound.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public GetNotificationResDto getNotifications(AuthenticatedMember authenticatedMember) {
        if (!memberRepository.existsById(authenticatedMember.getMemberId())) {
            throw new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID);
        }

        Notification notification = notificationRepository.findByOwnerId(authenticatedMember.getMemberId())
                .orElseThrow(() -> new NotificationException(NotificationErrorInfo.NOT_FOUND_NOTIFICATION_BY_OWNER));
        notificationRepository.updateReadTrue(authenticatedMember.getMemberId());

        return GetNotificationResDto.from(notification);
    }

    @Transactional
    public void sendNotification(NotificationEvent notificationEvent) {
        if (!notificationRepository.existsByOwnerId(notificationEvent.getOwnerId())) {
            notificationRepository.saveNotification(notificationEvent);
        }
        notificationRepository.saveNotificationItem(notificationEvent);
    }
}
