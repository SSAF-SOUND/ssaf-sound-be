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

import java.time.LocalDate;
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

    // 토 ~ 수요일 동안 03:00에 이틀 뒤의 웰스토리 메뉴를 스크래핑
    @Scheduled(cron = "0 0 3 ? * SAT,SUN,MON,THU,WED")
    public void scrapWelstory(){

        WelstoryInfoProvider welstoryInfoProvider = (WelstoryInfoProvider) scrapInfoProviderFactory.getProviderFrom("welstory");

        List<Lunch> lunches = new ArrayList<>();

        List<MetaData> campuses = new ArrayList<>();
        campuses.add(metaDataConsumer.getMetaData("CAMPUS","서울"));
        campuses.add(metaDataConsumer.getMetaData("CAMPUS","부울경"));
        campuses.add(metaDataConsumer.getMetaData("CAMPUS","구미"));

        // 이틀 뒤 메뉴를 가져오기 위한 일자 설정
        LocalDate menuDt = LocalDate.now().plusDays(2);


        // 로그인
        welstoryInfoProvider.setSessionHeader();

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

        // 일괄 저장
        lunchRepository.saveAll(lunches);
    }

    // 매주 토요일 03:02 프레시밀 스크래핑
    @Scheduled(cron = "0 2 3 ? * SAT")
    public void scrapFreshmeal(){
        ScrapInfoProvider freshmealInfoProvider = scrapInfoProviderFactory.getProviderFrom("freshmeal");

        List<Lunch> lunches = new ArrayList<>();
        // 일주일의 데이터를 한 번에 응답의 요일별 프로퍼티로 받는다.
        // 요일별 Key
        List<String> daysOfWeek = Arrays.asList("mo","tu","we","th","fr");

        MetaData campus = metaDataConsumer.getMetaData("CAMPUS","광주");

        for (String day : daysOfWeek) {

            // 캠퍼스 정보로 요청 dto 생성
            GetScrapReqDto request = GetScrapReqDto.builder()
                    .campus(campus)
                    .build();

            // 스크래핑 요청
            GetFreshmealResDto meals = (GetFreshmealResDto) freshmealInfoProvider.scrapLunchInfo(request);

            // json 형식에 따른 dto를 파싱하고, 리스트를 반복하면서 Lunch 객체를 얻어온다.
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

        // 일괄 저장
        lunchRepository.saveAll(lunches);
    }
}
