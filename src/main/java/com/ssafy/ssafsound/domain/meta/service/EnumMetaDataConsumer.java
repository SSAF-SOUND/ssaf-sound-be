package com.ssafy.ssafsound.domain.meta.service;

import com.ssafy.ssafsound.domain.meta.domain.*;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EnumMetaDataConsumer implements MetaDataConsumer {

    private final Map<String, Map<String, MetaData>> map;

    public EnumMetaDataConsumer() {
        this.map = new HashMap<>();
        this.putMetaData(MetaDataType.SKILL.name(), Skill.class);
        this.putMetaData(MetaDataType.CAMPUS.name(), Campus.class);
        this.putMetaData(MetaDataType.RECRUIT_TYPE.name(), RecruitType.class);
        this.putMetaData(MetaDataType.UPLOAD_DIRECTORY.name(), UploadDirectory.class);
        this.putMetaData(MetaDataType.CERTIFICATION.name(), Certification.class);
        this.putMetaData(MetaDataType.MAJOR_TRACK.name(), MajorTrack.class);
        this.putMetaData(MetaDataType.SOURCE_TYPE.name(), SourceType.class);
    }

    @Override
    public List<MetaData> getMetaDataList(String type) {
        Map<String, MetaData> metaDatas = this.getMetaMap(type.toLowerCase());
        return List.copyOf(metaDatas.values());
    }

    @Override
    public MetaData getMetaData(String type, String name) {
        MetaData metaData = this.getMetaMap(type.toLowerCase()).get(name.toLowerCase());

        if(metaData == null) {
            throw new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND);
        }
        return metaData;
    }

    @Override
    public void putMetaData(String data, Class<? extends MetaDataProvider> enumClass) {
        this.map.put(data.toLowerCase(), fromEnum(enumClass));
    }

    private Map<String, MetaData> getMetaMap(String type) {
        Map<String, MetaData> metaDataMap = map.get(type);

        if(metaDataMap == null) {
            throw new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND);
        }
        return metaDataMap;
    }

    private Map<String, MetaData> fromEnum(Class<? extends MetaDataProvider> enumClass) {
        Map<String, MetaData> metaDataMap = new HashMap<>();
        Arrays.stream(enumClass.getEnumConstants())
                .map(MetaData::new).forEach(metaData -> metaDataMap.put(metaData.getName().toLowerCase(), metaData));
        return Collections.unmodifiableMap(metaDataMap);
    }
}
