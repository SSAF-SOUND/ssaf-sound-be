package com.ssafy.ssafsound.domain.lunch.task.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@ToString
@Getter
@RequiredArgsConstructor
@Component
//@ConfigurationProperties(prefix = "scrap.freshmeal")
//@ConstructorBinding
public class FreshmealProperties {
    private final String url = "a";
    private final String storeIdx = "a";
    private final String weekType = "a";
}
