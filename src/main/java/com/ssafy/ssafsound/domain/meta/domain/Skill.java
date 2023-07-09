package com.ssafy.ssafsound.domain.meta.domain;

public enum Skill implements MetaDataProvider {
    SPRING(1, "Spring"),
    REACT(2, "React"),
    IOS(3, "IOS"),
    VUE(4, "Vue"),
    JAVA(5, "Java"),
    JAVA_SCRIPT(6, "JavaScript"),
    TYPE_SCRIPT(7, "TypeScript"),
    NODE_JS(8, "Nodejs"),
    NEXT_JS(9, "Nextjs"),
    NUXT_JS(10, "Nuxtjs"),
    XD(11, "XD"),
    SWIFT(12, "Swift"),
    FIGMA(13, "Figma"),
    SVELTE(14, "Svelte"),
    ANDROID(15, "Android"),
    FLUTTER(16, "Flutter"),
    DJANGO(17, "Django"),
    ETC(18, "기타");

    private int id;
    private String name;

    Skill(int id, String name) {
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
