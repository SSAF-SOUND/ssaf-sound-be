package com.ssafy.ssafsound.global.config;

import com.ssafy.ssafsound.domain.lunch.task.LunchScraper;
import com.ssafy.ssafsound.domain.lunch.task.domain.FreshmealProperties;
import com.ssafy.ssafsound.domain.lunch.task.domain.WelstoryProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({WelstoryProperties.class, FreshmealProperties.class})
@RequiredArgsConstructor
public class ScrapConfig {

    private final LunchScraper lunchScraper;

    @Bean
    ApplicationRunner applicationRunner() {
        return args ->{

            lunchScraper.scrapWelstory();
            lunchScraper.scrapFreshmeal();
        };
    }
}
