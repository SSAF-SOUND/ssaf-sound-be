package com.ssafy.ssafsound.domain.meta.domain;

public enum Campus implements MetaDataProvider {
    SEOUL(1, "서울"),
    DAEJEON(2, "대전"),
    GWANGJU(3, "광주"),
    GUMI(4, "구미"),
    BUSAN(5, "부울경");

    private int id;
    private String name;

    Campus(int id, String name) {
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
