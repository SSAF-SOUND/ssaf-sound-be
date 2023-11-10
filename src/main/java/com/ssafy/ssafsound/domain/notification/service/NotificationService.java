package com.ssafy.ssafsound.domain.notification.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.notification.domain.Notification;
import com.ssafy.ssafsound.domain.notification.dto.GetNotificationReqDto;
import com.ssafy.ssafsound.domain.notification.dto.GetNotificationResDto;
import com.ssafy.ssafsound.domain.notification.event.NotificationEvent;
import com.ssafy.ssafsound.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public GetNotificationResDto getNotifications(AuthenticatedMember authenticatedMember, GetNotificationReqDto getNotificationReqDto) {
        if (!memberRepository.existsById(authenticatedMember.getMemberId())) {
            throw new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID);
        }

        Long ownerId = authenticatedMember.getMemberId();
        Long cursor = getNotificationReqDto.getCursor();
        int size = getNotificationReqDto.getSize();
        List<Notification> notifications = notificationRepository.findAllByOwnerAndReadTrue(ownerId, cursor, size);
        return GetNotificationResDto.of(notifications, size);
    }

    @Transactional
    public void sendNotification(NotificationEvent notificationEvent) {
        Notification notification = Notification.from(notificationEvent);
        notificationRepository.save(notification);
    }
}
