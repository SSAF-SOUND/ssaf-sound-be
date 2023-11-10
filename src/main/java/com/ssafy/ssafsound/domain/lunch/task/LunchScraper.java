package com.ssafy.ssafsound.domain.lunch.task;


import com.ssafy.ssafsound.domain.lunch.service.LunchScrapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LunchScraper {

    private final LunchScrapService lunchScrapService;
    private final List<String> campusNames = List.of("서울", "부울경", "구미");

    // 토 ~ 수요일 동안 03:00에 이틀 뒤의 웰스토리 메뉴를 스크래핑
    @Scheduled(cron = "0 0 3 ? * SAT,SUN,MON,TUE,WED")
    public void scrapWelstory() {

        // 이틀 뒤 메뉴를 가져오기 위한 일자 설정
        LocalDate date = LocalDate.now().plusDays(2);

        for (String campusName : campusNames) {
            lunchScrapService.scrapWelstoryLunch(campusName, date);
        }
    }

    // 매주 토요일 03:02 프레시밀 스크래핑
    @Scheduled(cron = "0 2 3 ? * SAT")
    public void scrapFreshmeal() {

        lunchScrapService.scrapFreshmealLunch();
    }
}
