package com.ssafy.ssafsound.domain.notification.converter;

import com.ssafy.ssafsound.domain.notification.domain.NotificationType;

import javax.persistence.AttributeConverter;

public class NotificationTypeConverter implements AttributeConverter<NotificationType, String> {
    @Override
    public String convertToDatabaseColumn(NotificationType attribute) {
        return attribute.getName();
    }

    @Override
    public NotificationType convertToEntityAttribute(String dbData) {
        return NotificationType.ofName(dbData);
    }
}
