package com.ssafy.ssafsound.domain.lunch.task;


import com.ssafy.ssafsound.domain.lunch.repository.LunchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LunchScraper {

    private final LunchRepository lunchRepository;



    @Scheduled(cron = "0 0 0 ? ? 6")
    public void scrapWelstory(){

    }

    @Scheduled(cron = "0 0 0 ? ? 6")
    public void scrapFreshMeal(){

    }

}
