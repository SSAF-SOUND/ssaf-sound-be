package com.ssafy.ssafsound.domain.meta.converter;

import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SourceTypeConverter extends BaseConverter{

    @Autowired
    public SourceTypeConverter(MetaDataConsumer metaDataConsumer) {
        super(metaDataConsumer, MetaDataType.SOURCE_TYPE.name());
    }
}
