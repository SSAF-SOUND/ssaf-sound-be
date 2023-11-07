package com.ssafy.ssafsound.domain.notification.converter;

import com.ssafy.ssafsound.domain.notification.domain.ServiceType;

import javax.persistence.AttributeConverter;

public class ServiceTypeConverter implements AttributeConverter<ServiceType, String> {
    @Override
    public String convertToDatabaseColumn(ServiceType attribute) {
        return attribute.getName();
    }

    @Override
    public ServiceType convertToEntityAttribute(String dbData) {
        return ServiceType.ofName(dbData);
    }
}
