package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.meta.domain.MetaDataProvider;
import com.ssafy.ssafsound.domain.meta.domain.RecruitType;
import com.ssafy.ssafsound.domain.meta.domain.Skill;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductionMetaDataNameFixture {

    public static final List<String> skills = convertEnumToMetaDataList(Skill.class);
    public static final List<String> recruitTypes = convertEnumToMetaDataList(RecruitType.class);

    private static List<String> convertEnumToMetaDataList(Class<? extends MetaDataProvider> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).map(MetaDataProvider::getName).collect(Collectors.toList());
    }
}
