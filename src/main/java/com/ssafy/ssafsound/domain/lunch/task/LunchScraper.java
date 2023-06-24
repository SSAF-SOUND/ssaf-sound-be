package com.ssafy.ssafsound.domain.lunch.task;


import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.repository.LunchRepository;
import com.ssafy.ssafsound.domain.lunch.task.domain.ScrapInfoProvider;
import com.ssafy.ssafsound.domain.lunch.task.domain.ScrapInfoProviderFactory;
import com.ssafy.ssafsound.domain.lunch.task.domain.WelstoryInfoProvider;
import com.ssafy.ssafsound.domain.lunch.task.dto.GetFreshmealResDto;
import com.ssafy.ssafsound.domain.lunch.task.dto.GetScrapReqDto;
import com.ssafy.ssafsound.domain.lunch.task.dto.GetWelstoryResDto;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LunchScraper {

    private final LunchRepository lunchRepository;
    private final MetaDataConsumer metaDataConsumer;
    private final ScrapInfoProviderFactory scrapInfoProviderFactory;

    @Scheduled(cron = "0 0 0 * * 6")
    public void scrapWelstory(){
        WelstoryInfoProvider welstoryInfoProvider = (WelstoryInfoProvider) scrapInfoProviderFactory.getProviderFrom("welstory");

        List<MetaData> campuses = new ArrayList<>();
        campuses.add(metaDataConsumer.getMetaData("CAMPUS","서울"));
        campuses.add(metaDataConsumer.getMetaData("CAMPUS","부울경"));
        campuses.add(metaDataConsumer.getMetaData("CAMPUS","구미"));

        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        LocalDate endDate = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
//        LocalDate endDate = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY));

        List<Lunch> lunches = new ArrayList<>();

        welstoryInfoProvider.setSessionHeader();

        for(LocalDate menuDt = startDate; menuDt.isBefore(endDate); menuDt = menuDt.plusDays(1)){
            for (MetaData campus : campuses) {
                try {
                    // 캠퍼스 + 날짜로 스크랩 요청을 위한 파라미터 dto 생성
                    GetScrapReqDto request = GetScrapReqDto.builder()
                            .campus(campus)
                            .menuDt(menuDt)
                            .build();

                    // 스크랩 해오기
                    GetWelstoryResDto meals = (GetWelstoryResDto) welstoryInfoProvider.scrapLunchInfo(request);

                    // 결과 저장
                    meals.getWelstoryBodyData().getMealList().forEach(
                            meal -> {
                                Lunch lunch = meal.toEntity(campus);

                                if (lunch != null) {
                                    lunches.add(lunch);
                                }
                            }
                    );

                } catch (Exception e) {
                    log.error(e.getMessage());
                    e.printStackTrace();

                    throw new RuntimeException(e);
                }
            }
        }

        lunchRepository.saveAll(lunches);
    }

    @Scheduled(cron = "0 0 0 * * 6")
    public void scrapFreshmeal(){
        ScrapInfoProvider freshmealInfoProvider = scrapInfoProviderFactory.getProviderFrom("freshmeal");

        List<String> daysOfWeek = Arrays.asList("mo","tu","we","th","fr");
        MetaData campus = metaDataConsumer.getMetaData("CAMPUS","광주");
        List<Lunch> lunches = new ArrayList<>();

        for (String day : daysOfWeek) {

            GetScrapReqDto request = GetScrapReqDto.builder()
                    .campus(campus)
                    .build();

            GetFreshmealResDto meals = (GetFreshmealResDto) freshmealInfoProvider.scrapLunchInfo(request);

            meals.getData().getDailyMeal().get(day).getMeals().forEach(
                    meal -> {
                        try {
                            Lunch lunch = meal.toEntity(campus);
                            if (lunch != null) {
                                lunches.add(lunch);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            throw new RuntimeException(e.getMessage());
                        }
                    }
            );
        }

        lunchRepository.saveAll(lunches);
    }
}
