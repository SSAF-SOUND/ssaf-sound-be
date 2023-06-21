package com.ssafy.ssafsound.domain.lunch.task;


import com.ssafy.ssafsound.domain.lunch.dto.GetLunchScrapResDto;
import com.ssafy.ssafsound.domain.lunch.repository.LunchRepository;
import com.ssafy.ssafsound.domain.lunch.task.domain.ScrapInfoProvider;
import com.ssafy.ssafsound.domain.lunch.task.domain.ScrapInfoProviderFactory;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
        ScrapInfoProvider welstoryInfoProvider = scrapInfoProviderFactory.getProviderFrom("welstory");

        List<MetaData> campuses = new ArrayList<>();
        campuses.add(metaDataConsumer.getMetaData("CAMPUS","서울"));
        campuses.add(metaDataConsumer.getMetaData("CAMPUS","부울경"));
        campuses.add(metaDataConsumer.getMetaData("CAMPUS","구미"));


        campuses.stream()
                .forEach(campus -> {
                    try {
                        JSONArray meals = welstoryInfoProvider.scrapLunchInfo(campus)
                                .getJSONObject("data")
                                .getJSONArray("menuList");

                        meals.forEach((meal) -> {
                            lunchRepository.save(GetLunchScrapResDto.fromWelstoryResponse((JSONObject) meal, campus).to());
                        });
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        e.printStackTrace();

                        throw new RuntimeException(e);
                    }
                });
    }

    @Scheduled(cron = "0 0 0 * * 6")
    public void scrapFreshmeal(){
        ScrapInfoProvider freshmealInfoProvider = scrapInfoProviderFactory.getProviderFrom("freshmeal");

        List<String> daysOfWeek = Arrays.asList("mo","tu","we","th","fr");
        MetaData campus = metaDataConsumer.getMetaData("CAMPUS","광주");

        JSONObject data = freshmealInfoProvider.scrapLunchInfo(campus);

        daysOfWeek.forEach((day) -> {
            data.getJSONObject(day).getJSONArray("2").forEach((meal)->{
                lunchRepository.save(GetLunchScrapResDto.fromFreshmealResponse((JSONObject) meal, campus).to());
            });
        });
    }

}
