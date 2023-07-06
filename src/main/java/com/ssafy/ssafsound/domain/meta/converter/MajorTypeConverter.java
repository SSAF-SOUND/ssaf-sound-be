package com.ssafy.ssafsound.domain.meta.converter;

import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Converter;

@Converter
public class MajorTypeConverter extends BaseConverter {

    @Autowired
    public MajorTypeConverter(MetaDataConsumer metaDataConsumer) {
        super(metaDataConsumer, MetaDataType.MAJOR_TYPE.name());
    }
}
