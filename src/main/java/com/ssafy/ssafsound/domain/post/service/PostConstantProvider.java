package com.ssafy.ssafsound.domain.post.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.constant.post")
@ConstructorBinding
public class PostConstantProvider {
    private final Long HOT_POST_LIKES_THRESHOLD;
}
