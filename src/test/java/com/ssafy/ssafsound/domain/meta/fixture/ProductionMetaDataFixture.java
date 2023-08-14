package com.ssafy.ssafsound.domain.meta.fixture;

import com.ssafy.ssafsound.domain.meta.domain.*;

import java.util.*;
import java.util.stream.Collectors;

public class ProductionMetaDataFixture {

    public static final List<MetaData> CAMPUSES = convertEnumToMetaDataList(Campus.class);
    public static final List<MetaData> SKILLS = convertEnumToMetaDataList(Skill.class);
    public static final List<MetaData> RECRUIT_TYPES = convertEnumToMetaDataList(RecruitType.class);

    private static List<MetaData> convertEnumToMetaDataList(Class<? extends MetaDataProvider> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).map(MetaData::new).collect(Collectors.toList());
    }
}
