package com.ssafy.ssafsound.global.config;

import com.ssafy.ssafsound.domain.lunch.task.domain.FreshmealProperties;
import com.ssafy.ssafsound.domain.lunch.task.domain.WelstoryProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({WelstoryProperties.class, FreshmealProperties.class})
@RequiredArgsConstructor
public class ScrapConfig {
}
