package com.ssafy.ssafsound.domain.meta.service;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataProvider;

import java.util.List;

public interface MetaDataConsumer {
    List<MetaData> getMetaDataList(String type);
    MetaData getMetaData(String type, String name);
    void putMetaData(String data, Class<? extends MetaDataProvider> enumClass);
}
