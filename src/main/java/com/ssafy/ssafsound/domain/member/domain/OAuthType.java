package com.ssafy.ssafsound.domain.member.domain;

public enum OAuthType {
    GOOGLE("google"), GITHUB("github"), KAKAO("kakao"), APPLE("apple");

    private final String oauthName;

    OAuthType(String oauthName) {
        this.oauthName = oauthName;
    }

    public boolean isEqual(String oauthName) {
        return this.oauthName.equals(oauthName);
    }
}
