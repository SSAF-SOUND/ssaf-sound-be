package com.ssafy.ssafsound.domain.notification.dto;

import com.ssafy.ssafsound.domain.notification.domain.Notification;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetNotificationOffsetResDto {
    private List<GetNotificationElement> notifications;
    private Integer currentPage;
    private Integer totalPageCount;

    public static GetNotificationOffsetResDto of(Page<Notification> notifications) {
        return GetNotificationOffsetResDto.builder()
                .notifications(notifications.getContent().stream()
                        .map(GetNotificationElement::from)
                        .collect(Collectors.toList()))
                .currentPage(notifications.getNumber() + 1)
                .totalPageCount(notifications.getTotalPages())
                .build();

    }
}
