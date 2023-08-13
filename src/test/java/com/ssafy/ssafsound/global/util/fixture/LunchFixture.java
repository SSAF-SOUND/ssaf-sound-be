package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListElementResDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListReqDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListResDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchResDto;
import com.ssafy.ssafsound.domain.meta.domain.Campus;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;

import java.time.LocalDate;
import java.util.List;

public class LunchFixture {

    public static final GetLunchListReqDto GET_LUNCH_LIST_REQ_DTO = GetLunchListReqDto.builder()
            .date(LocalDate.parse("2023-08-12"))
            .campus("서울")
            .build();

    public static final Lunch LUNCH_FIXTURE1 = Lunch.builder()
            .id(1L)
            .campus(new MetaData(Campus.SEOUL))
            .createdAt(LocalDate.parse("2023-08-12"))
            .course("한식")
            .mainMenu("메인메뉴" + 1)
            .extraMenu("메뉴1, 메뉴2, 메뉴3")
            .sumKcal("1,544 kcal")
            .imagePath("www.sample" + 1 + ".png")
            .build();

    public static final Lunch LUNCH_FIXTURE2 = Lunch.builder()
            .id(2L)
            .campus(new MetaData(Campus.SEOUL))
            .createdAt(LocalDate.parse("2023-08-12"))
            .course("일품")
            .mainMenu("메인메뉴" + 2)
            .extraMenu("메뉴1, 메뉴2, 메뉴3")
            .sumKcal("1,994 kcal")
            .imagePath("www.sample" + 2 + ".png")
            .build();

    public static final Lunch LUNCH_FIXTURE3 = Lunch.builder()
            .id(3L)
            .campus(new MetaData(Campus.SEOUL))
            .createdAt(LocalDate.parse("2023-08-12"))
            .course("A코스")
            .mainMenu("메인메뉴" + 3)
            .extraMenu("메뉴1, 메뉴2, 메뉴3")
            .sumKcal("2,012 kcal")
            .imagePath("www.sample" + 3 + ".png")
            .build();

    public static final GetLunchListElementResDto GET_LUNCH_LIST_ELEMENT_RES_DTO1 = GetLunchListElementResDto.of(LUNCH_FIXTURE1, 29L);
    public static final GetLunchListElementResDto GET_LUNCH_LIST_ELEMENT_RES_DTO2 = GetLunchListElementResDto.of(LUNCH_FIXTURE1, 22L);
    public static final GetLunchListElementResDto GET_LUNCH_LIST_ELEMENT_RES_DTO3 = GetLunchListElementResDto.of(LUNCH_FIXTURE1, 17L);

    public static final GetLunchListResDto GET_LUNCH_LIST_RES_DTO = GetLunchListResDto.of(
            List.of(GET_LUNCH_LIST_ELEMENT_RES_DTO1,
                    GET_LUNCH_LIST_ELEMENT_RES_DTO2,
                    GET_LUNCH_LIST_ELEMENT_RES_DTO3
            ), 2L);

    public static final GetLunchResDto GET_LUNCH_RES_DTO = GetLunchResDto.of(LUNCH_FIXTURE1);

}
