package com.ssafy.ssafsound.domain.notification.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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

    private Integer owner;

    @Field
    @Builder.Default
    private List<NotificationElement> notificationElements = new ArrayList<>();

}
