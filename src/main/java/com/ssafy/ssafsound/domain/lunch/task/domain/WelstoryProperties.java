package com.ssafy.ssafsound.domain.lunch.task.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
@Component
//@ConfigurationProperties(prefix = "scrap.welstory")
//@ConstructorBinding
public class WelstoryProperties {

    private final Credentials credentials;
    private final Map<String, String> info;
    private final Map<String, String> restaurantCode;

    @Getter
    @ToString
//    @RequiredArgsConstructor
//    @ConstructorBinding
    @Component
    public static class Credentials{
        private final String url = "a";
        private final String username = "a";
        private final String password = "a";
        private final String rememberMe = "a";
    }
}
