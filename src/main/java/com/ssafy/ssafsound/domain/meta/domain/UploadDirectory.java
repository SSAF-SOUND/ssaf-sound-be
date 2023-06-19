package com.ssafy.ssafsound.domain.meta.domain;

public enum UploadDirectory implements MetaDataProvider{

    POST(1,"post"),
    AVATAR(2,"avatar"),
    STATIC(3,"static");

    private int id;
    private String name;

    UploadDirectory(int id, String name) {
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
