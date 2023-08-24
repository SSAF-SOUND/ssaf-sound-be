package com.ssafy.ssafsound.domain.meta.domain;

public enum SourceType implements MetaDataProvider {
    POST(1, "post"),
    COMMENT(2,"comment"),
    CHAT(3, "chat"),
    RECRUIT(4,"recruit"),
    MEMBER(5,"member"),
    LUNCH(6,"lunch"),
    BOARD(7,"board"),
    ALARM(8,"alarm"),
    RECRUIT_COMMENT(9, "recruit_comment");

    private int id;

    private String name;

    SourceType(int id, String name) {
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
