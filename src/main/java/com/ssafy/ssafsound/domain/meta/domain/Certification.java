package com.ssafy.ssafsound.domain.meta.domain;

public enum Certification implements  MetaDataProvider {
    ONE_SEMESTER(1, "선물"),
    TWO_SEMESTER(2, "하나"),
    THREE_SEMESTER(3, "출발"),
    FOUR_SEMESTER(4, "충전"),
    FIVE_SEMESTER(5, "극복"),
    SIX_SEMESTER(6, "hot식스"),
    SEVEN_SEMESTER(7, "럭키"),
    EIGHT_SEMESTER(8, "칠전팔"),
    NINE_SEMESTER(8, "great"),
    TEN_SEMESTER(10, "텐션");


    private int id;
    private String name;

    Certification(int id, String name) {
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
