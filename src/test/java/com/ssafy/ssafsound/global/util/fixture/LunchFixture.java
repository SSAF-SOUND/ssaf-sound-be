package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.domain.LunchPoll;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListElementResDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListReqDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListResDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchResDto;
import com.ssafy.ssafsound.domain.lunch.dto.PostLunchPollResDto;
import com.ssafy.ssafsound.domain.meta.domain.Campus;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;

import java.time.LocalDate;
import java.util.List;

import static com.ssafy.ssafsound.global.util.fixture.MemberFixture.MEMBER_SHERYL;
import static com.ssafy.ssafsound.global.util.fixture.MemberFixture.MEMBER_WALTER;

public class LunchFixture {

    public static final String DATE_FIXTURE = "2023-08-12";
    public static final LocalDate LOCAL_DATE_FIXTURE = LocalDate.parse(DATE_FIXTURE);

    public static final String LUNCH_IMAGE_FIXTURE1 = "http://samsungwelstory.com/data/manager/recipe/E110/20230814/s20230727144547.png";
    public static final String LUNCH_IMAGE_FIXTURE2 = "http://samsungwelstory.com/data/manager/recipe/E110/20230814/s20230802100210.png";
    public static final String LUNCH_IMAGE_FIXTURE3 = "http://samsungwelstory.com/data/manager/recipe/E110/20230811/s20220325094317.jpg";
    public static final String LUNCH_IMAGE_FIXTURE4 = "http://samsungwelstory.com/data/manager/recipe/E21F/20230814/s20230814164703.jpg";


    public static final GetLunchListReqDto GET_LUNCH_LIST_REQ_DTO = GetLunchListReqDto.builder()
            .date(LocalDate.parse("2023-08-12"))
            .campus("서울")
            .build();

    public static final Lunch LUNCH_FIXTURE1 = Lunch.builder()
            .id(1L)
            .campus(new MetaData(Campus.SEOUL))
            .createdAt(LOCAL_DATE_FIXTURE)
            .course("한식")
            .mainMenu("메인메뉴" + 1)
            .extraMenu("메뉴1, 메뉴2, 메뉴3")
            .sumKcal("1,544 kcal")
            .imagePath(LUNCH_IMAGE_FIXTURE1)
            .build();

    public static final Lunch LUNCH_FIXTURE2 = Lunch.builder()
            .id(2L)
            .campus(new MetaData(Campus.SEOUL))
            .createdAt(LocalDate.parse("2023-08-12"))
            .course("일품")
            .mainMenu("메인메뉴" + 2)
            .extraMenu("메뉴1, 메뉴2, 메뉴3")
            .sumKcal("1,994 kcal")
            .imagePath(LUNCH_IMAGE_FIXTURE2)
            .build();

    public static final Lunch LUNCH_FIXTURE3 = Lunch.builder()
            .id(3L)
            .campus(new MetaData(Campus.SEOUL))
            .createdAt(LocalDate.parse("2023-08-12"))
            .course("A코스")
            .mainMenu("메인메뉴" + 3)
            .extraMenu("메뉴1, 메뉴2, 메뉴3")
            .sumKcal("2,012 kcal")
            .imagePath(LUNCH_IMAGE_FIXTURE3)
            .build();

    public static final Lunch LUNCH_FIXTURE4 = Lunch.builder()
            .id(4L)
            .campus(new MetaData(Campus.GUMI))
            .createdAt(LocalDate.parse("2023-08-12"))
            .course("저녁B코너")
            .mainMenu("부대찌개")
            .extraMenu("부대찌개,꽈리돈육조림,봉어묵볶음,해초무생채")
            .sumKcal("524")
            .imagePath(LUNCH_IMAGE_FIXTURE4)
            .build();

    public static final GetLunchListElementResDto GET_LUNCH_LIST_ELEMENT_RES_DTO1 = GetLunchListElementResDto.of(LUNCH_FIXTURE1, 29L);
    public static final GetLunchListElementResDto GET_LUNCH_LIST_ELEMENT_RES_DTO2 = GetLunchListElementResDto.of(LUNCH_FIXTURE2, 22L);
    public static final GetLunchListElementResDto GET_LUNCH_LIST_ELEMENT_RES_DTO3 = GetLunchListElementResDto.of(LUNCH_FIXTURE3, 17L);

    public static final GetLunchListResDto GET_LUNCH_LIST_RES_DTO = GetLunchListResDto.of(
            List.of(GET_LUNCH_LIST_ELEMENT_RES_DTO1,
                    GET_LUNCH_LIST_ELEMENT_RES_DTO2,
                    GET_LUNCH_LIST_ELEMENT_RES_DTO3
            ), 1L);

    public static final GetLunchResDto GET_LUNCH_RES_DTO = GetLunchResDto.of(LUNCH_FIXTURE1);

    public static final LunchPoll LUNCH_POLL_FIXTURE1 = LunchPoll.builder()
            .id(1L)
            .lunch(LUNCH_FIXTURE2)
            .member(MEMBER_WALTER)
            .polledAt(LOCAL_DATE_FIXTURE)
            .build();

    public static final LunchPoll LUNCH_POLL_FIXTURE2 = LunchPoll.builder()
            .id(2L)
            .lunch(LUNCH_FIXTURE1)
            .member(MEMBER_SHERYL)
            .polledAt(LOCAL_DATE_FIXTURE)
            .build();

    public static final LunchPoll LUNCH_POLL_FIXTURE3 = LunchPoll.builder()
            .id(3L)
            .lunch(LUNCH_FIXTURE4)
            .member(MEMBER_WALTER)
            .polledAt(LOCAL_DATE_FIXTURE)
            .build();

    public static final PostLunchPollResDto POST_LUNCH_POLL_RES_DTO = PostLunchPollResDto.of(4327L);
}
