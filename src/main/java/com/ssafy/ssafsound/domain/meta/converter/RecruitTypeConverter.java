package com.ssafy.ssafsound.domain.meta.converter;

import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Converter;

@Converter
public class RecruitTypeConverter extends BaseConverter {

    @Autowired
    public RecruitTypeConverter(MetaDataConsumer metaDataConsumer) {
        super(metaDataConsumer, MetaDataType.RECRUIT_TYPE.name());
    }
}
