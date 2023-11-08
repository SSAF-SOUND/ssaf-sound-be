package com.ssafy.ssafsound.domain.notification.dto;

import com.ssafy.ssafsound.domain.notification.domain.NotificationType;
import com.ssafy.ssafsound.domain.notification.domain.ServiceType;
import com.ssafy.ssafsound.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateNotification {
    private Long ownerId;
    private String message;
    private Long contentId;
    private ServiceType serviceType;
    private NotificationType notificationType;

    public static CreateNotification postReplyNotificationFrom(Post post) {
        String message = String.format("'%s' 게시글에 새로운 댓글이 달렸습니다.", post.getTitle());

        return CreateNotification.builder()
                .ownerId(post.getMember().getId())
                .message(message)
                .contentId(post.getId())
                .serviceType(ServiceType.POST)
                .notificationType(NotificationType.POST_REPLAY)
                .build();
    }
}
