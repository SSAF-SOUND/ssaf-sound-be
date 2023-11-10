package com.ssafy.ssafsound.domain.lunch.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.exception.AuthErrorInfo;
import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListReqDto;
import com.ssafy.ssafsound.domain.lunch.exception.LunchErrorInfo;
import com.ssafy.ssafsound.domain.lunch.exception.LunchException;
import com.ssafy.ssafsound.domain.lunch.repository.LunchRepository;
import com.ssafy.ssafsound.domain.lunch.dto.GetFreshmealResDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetScrapReqDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetWelstoryResDto;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.meta.domain.Campus;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class LunchScrapService {

    private final LunchRepository lunchRepository;
    private final MetaDataConsumer metaDataConsumer;
    private final ScrapInfoProviderFactory scrapInfoProviderFactory;

    @Transactional(readOnly = false)
    public void scrapLunchManually(AuthenticatedMember authenticatedMember, GetLunchListReqDto getLunchListReqDto) {

        String memberRole = Objects.requireNonNull(authenticatedMember.getMemberRole(), MemberErrorInfo.MEMBER_ROLE_TYPE_NOT_FOUND.getMessage());

        if (!memberRole.equals("admin")) {
            throw new AuthException(AuthErrorInfo.UNAUTHORIZED_ERROR);
        }

        if (getLunchListReqDto.equals("광주")) {
            scrapFreshmealLunch();
        } else if (getLunchListReqDto.equals("대전")) {
            throw new LunchException(LunchErrorInfo.SCRAPING_ERROR);
        } else {
            scrapWelstoryLunch(getLunchListReqDto.getCampus(), getLunchListReqDto.getDate());
        }
    }

    @Transactional(readOnly = false)
    public void scrapWelstoryLunch(String campusName, LocalDate date) {

        if (!(campusName.equals(Campus.SEOUL.getName()) || campusName.equals(Campus.GUMI.getName()) || campusName.equals(Campus.BUSAN.getName()))) {
            throw new LunchException(LunchErrorInfo.SCRAPING_ERROR);
        }

        MetaData campus = metaDataConsumer.getMetaData("CAMPUS", campusName);

        WelstoryInfoProvider welstoryInfoProvider = (WelstoryInfoProvider) scrapInfoProviderFactory.getProviderFrom("welstory");
        welstoryInfoProvider.setSessionHeader();

        List<Lunch> lunches = new ArrayList<>();

        GetScrapReqDto request = GetScrapReqDto.builder()
                .campus(campus)
                .menuDt(date)
                .build();

        try {
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
            throw new LunchException(LunchErrorInfo.SCRAPING_ERROR);
        }

        lunchRepository.saveAll(lunches);
    }

    @Transactional(readOnly = false)
    public void scrapFreshmealLunch() {
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
                            throw new LunchException(LunchErrorInfo.SCRAPING_ERROR);
                        }
                    }
            );
        }

        lunchRepository.saveAll(lunches);
    }
}
