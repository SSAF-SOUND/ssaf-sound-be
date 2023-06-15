package com.ssafy.ssafsound.domain.meta.domain;

public enum Skill implements MetaDataProvider {
    SPRING(1, "Spring Boot"),
    REACT(2, "React"),
    IOS(3, "IOS"),
    VUE(4, "Vue"),
    JAVA(5, "Vue"),
    JAVA_SCRIPT(6, "Vue"),
    TYPE_SCRIPT(7, "Vue"),
    NODE_JS(8, "Node"),
    NEXT_JS(9, "Node"),
    NUXT_JS(10, "Node"),
    ETC(11, "기타");

    private int id;
    private String name;

    Skill(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
