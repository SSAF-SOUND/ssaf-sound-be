package com.ssafy.ssafsound.domain.meta.converter;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.exception.MetaDataIntegrityException;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;

@Slf4j
public class BaseConverter implements AttributeConverter<MetaData, String> {

    private final MetaDataConsumer metaDataConsumer;
    private final String metaDataType;

    public BaseConverter(MetaDataConsumer metaDataConsumer, String metaDataType) {
        this.metaDataConsumer = metaDataConsumer;
        this.metaDataType = metaDataType;
    }

    @Override
    public String convertToDatabaseColumn(MetaData attribute) {
        if(attribute == null) return null;
        return attribute.getName();
    }

    @Override
    public MetaData convertToEntityAttribute(String dbData) {
        if(dbData == null) return null;

        MetaData metaData;
        try {
            metaData  = metaDataConsumer.getMetaData(metaDataType, dbData);
        } catch (ResourceNotFoundException e) {
            log.error("Data Integrity error: fail to convert {}", metaDataType);
            throw new MetaDataIntegrityException(GlobalErrorInfo.INTERNAL_SERVER_ERROR);
        }
        return metaData;
    }
}
