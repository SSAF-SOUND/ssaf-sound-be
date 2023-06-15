package com.ssafy.ssafsound.domain.meta.converter;

import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Converter;

@Converter
public class CampusConverter extends BaseConverter {

    @Autowired
    public CampusConverter(MetaDataConsumer metaDataConsumer) {
        super(metaDataConsumer, MetaDataType.CAMPUS.name());
    }
}
