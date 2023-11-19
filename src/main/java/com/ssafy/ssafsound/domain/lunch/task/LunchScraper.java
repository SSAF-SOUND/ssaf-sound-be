package com.ssafy.ssafsound.domain.lunch.task;


import com.ssafy.ssafsound.domain.lunch.service.LunchScrapService;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class LunchScraper {

    private final LunchScrapService lunchScrapService;
    private final MetaDataConsumer metaDataConsumer;

    // 토 ~ 수요일 동안 03:00에 이틀 뒤의 웰스토리 메뉴를 스크래핑
    @Scheduled(cron = "0 0 3 ? * SAT,SUN,MON,TUE,WED")
    public void scrapWelstory() {

        // 이틀 뒤 메뉴를 가져오기 위한 일자 설정
        LocalDate date = LocalDate.now().plusDays(2);

        lunchScrapService.scrapWelstoryLunch(date,
                metaDataConsumer.getMetaData("CAMPUS", "서울"),
                metaDataConsumer.getMetaData("CAMPUS", "부울경"),
                metaDataConsumer.getMetaData("CAMPUS", "구미")
        );

    }

    // 매주 토요일 03:02 프레시밀 스크래핑
    @Scheduled(cron = "0 2 3 ? * SAT")
    public void scrapFreshmeal() {

        lunchScrapService.scrapFreshmealLunch(metaDataConsumer.getMetaData("CAMPUS", "광주"));
    }


}
