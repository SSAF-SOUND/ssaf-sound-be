package com.ssafy.ssafsound.domain.meta.domain;

public enum RecruitType implements MetaDataProvider {
    STUDY(0, "스터디"),
    DESIGN(1, "기획/디자인"),
    FRONT_END(2, "프론트엔드"),
    BACK_END(3, "백엔드"),
    APP(4, "앱");

    private int id;
    private String name;

    RecruitType(int id, String name) {
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
