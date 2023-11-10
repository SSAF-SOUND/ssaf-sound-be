package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.notification.domain.Notification;
import com.ssafy.ssafsound.domain.notification.domain.NotificationType;
import com.ssafy.ssafsound.domain.notification.domain.ServiceType;

import java.time.LocalDateTime;

public class NotificationFixture {

    public Notification createPostReplyNotification() {
        return Notification.builder()
                .id(1L)
                .ownerId(1L)
                .message("'오늘 점심 추천좀' 게시글에 새로운 댓글이 달렸습니다.")
                .contentId(1L)
                .serviceType(ServiceType.POST)
                .notificationType(NotificationType.POST_REPLAY)
                .createdAt(LocalDateTime.now())
                .build();

    }

    public Notification createCommentReplyNotification() {
        return Notification.builder()
                .id(2L)
                .ownerId(1L)
                .message("'취업 하고싶다~~' 게시글에 새로운 대댓글이 달렸습니다.")
                .contentId(2L)
                .serviceType(ServiceType.POST)
                .notificationType(NotificationType.COMMENT_REPLAY)
                .createdAt(LocalDateTime.now().minusHours(1))
                .build();

    }


}
