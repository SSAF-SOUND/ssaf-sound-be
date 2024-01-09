package com.ssafy.ssafsound.domain.notification.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.notification.domain.Notification;
import com.ssafy.ssafsound.domain.notification.dto.*;
import com.ssafy.ssafsound.domain.notification.event.NotificationEvent;
import com.ssafy.ssafsound.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public GetNotificationCursorResDto getNotificationsByCursor(AuthenticatedMember authenticatedMember, GetNotificationCursorReqDto getNotificationCursorReqDto) {
        if (!memberRepository.existsById(authenticatedMember.getMemberId())) {
            throw new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID);
        }

        Long ownerId = authenticatedMember.getMemberId();
        Long cursor = getNotificationCursorReqDto.getCursor();
        int size = getNotificationCursorReqDto.getSize();
        List<Notification> notifications = notificationRepository.findAllByOwnerId(ownerId, cursor, size);
        notificationRepository.updateReadTrueByOwnerId(ownerId);

        return GetNotificationCursorResDto.of(notifications, size);
    }

    @Transactional
    public GetNotificationOffsetResDto getNotificationsByOffset(AuthenticatedMember authenticatedMember, GetNotificationOffsetReqDto getNotificationOffsetReqDto) {
        if (!memberRepository.existsById(authenticatedMember.getMemberId())) {
            throw new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID);
        }

        Long ownerId = authenticatedMember.getMemberId();
        PageRequest pageRequest = getNotificationOffsetReqDto.toPageRequest();
        Page<Notification> notifications = notificationRepository.findAllByOwnerId(ownerId, pageRequest);
        notificationRepository.updateReadTrueByOwnerId(ownerId);
        return GetNotificationOffsetResDto.of(notifications);
    }

    @Transactional
    public void sendNotification(NotificationEvent notificationEvent) {
        Notification notification = Notification.from(notificationEvent);
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public GetCheckNotificationResDto checkNotification(AuthenticatedMember authenticatedMember) {
        Boolean isNew = notificationRepository.existsNewNotification(authenticatedMember.getMemberId());

        return new GetCheckNotificationResDto(isNew);
    }
}
