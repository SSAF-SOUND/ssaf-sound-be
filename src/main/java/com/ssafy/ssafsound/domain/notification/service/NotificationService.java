package com.ssafy.ssafsound.domain.notification.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.notification.domain.Notification;
import com.ssafy.ssafsound.domain.notification.dto.GetNotificationResDto;
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

//    @Transactional
//    public Void saveNotification(CreateNotification createNotification) {
//        Long ownerId = createNotification.getOwnerId();
//
//        Optional<Notification> optionalNotification = notificationRepository.findByOwnerId(ownerId);
//        if (!notificationRepository.existsByOwnerId(ownerId)) {
////            notificationRepository.saveNotification(createNotification);
//        }
//
////        Notification notification = Notification.builder()
////                .owner(1L)
////                .build();
////        notificationRepository.save(notification);
//        notificationRepository.updateTemp(ownerId);
//
//
////        Notification notification = Notification.builder()
////                .owner(id)
////                .message("테스트 메시지")
////                .contentId(1L)
////                .serviceType(ServiceType.POST)
////                .notificationType(NotificationType.POST)
////                .build();
////
////        notificationRepository.saveTemp(notification);
//
////        throw new RuntimeException();
////        return null;
//    }
}
