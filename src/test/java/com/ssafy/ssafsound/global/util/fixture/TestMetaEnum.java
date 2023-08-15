package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.meta.domain.MetaDataProvider;

public enum TestMetaEnum implements MetaDataProvider {

    TEST_META_FIXTURE1(1, "메타데이터1"),
    TEST_META_FIXTURE2(2, "메타데이터2"),
    TEST_META_FIXTURE3(3, "메타데이터3"),
    TEST_META_FIXTURE4(4, "메타데이터4");

    private int id;
    private String name;

    TestMetaEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
