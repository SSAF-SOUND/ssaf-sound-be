package com.ssafy.ssafsound.domain.notification.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;

    @Indexed(unique = true)
    private Long ownerId;

    @Builder.Default
    private List<NotificationItem> notificationItems = new ArrayList<>();
}
