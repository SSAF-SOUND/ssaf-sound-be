package com.ssafy.ssafsound.domain.meta.domain;

public enum MajorType implements MetaDataProvider {
    EMBEDDED(1, "Embedded"),
    MOBILE(2, "Mobile"),
    PYTHON(3, "Python"),
    JAVA(4, "Java");

    private int id;
    private String name;

    MajorType(int id, String name) {
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
