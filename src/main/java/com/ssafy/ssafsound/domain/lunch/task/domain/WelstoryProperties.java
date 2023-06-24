package com.ssafy.ssafsound.domain.lunch.task.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "scrap.welstory")
@ConstructorBinding
public class WelstoryProperties {

    private final Credentials credentials;
    private final Map<String, String> info;
    private final Map<String, String> restaurantCode;

    @Getter
    @ToString
    @RequiredArgsConstructor
    @ConstructorBinding
    public static class Credentials{
        private final String url;
        private final String username;
        private final String password;
        private final String rememberMe;
    }
}
