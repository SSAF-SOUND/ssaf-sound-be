package com.ssafy.ssafsound.domain.lunch.task.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ToString
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "scrap.freshmeal")
@ConstructorBinding
public class FreshmealProperties {
    private final String url;
    private final String storeIdx;
    private final String weekType;
}
