package com.ssafy.ssafsound.domain.lunch.service;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.domain.LunchPoll;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListElementResDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListReqDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListResDto;
import com.ssafy.ssafsound.domain.lunch.repository.LunchPollRepository;
import com.ssafy.ssafsound.domain.lunch.repository.LunchRepository;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.meta.domain.Campus;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LunchServiceTest {

    @Mock
    private LunchRepository lunchRepository;
    @Mock
    private LunchPollRepository lunchPollRepository;
    @Mock
    private MetaDataConsumer metaDataConsumer;

    @InjectMocks
    private LunchService lunchService;

    private Lunch lunch1;
    private Lunch lunch2;
    private Lunch lunch3;
    private Lunch lunch4;
    private Member member1;
    private Member member2;
    private LunchPoll lunchPoll1;
    private LunchPoll lunchPoll2;
    private List<String> mainMenus;
    private LocalDate today = LocalDate.now();
    private LocalDate tomorrow = today.plusDays(1);
    private MetaData campus1;
    private MetaData campus2;

    @BeforeEach
    void setUp() {
        mainMenus = new ArrayList<>();

        // 테스트용 코스명 생성
        for (int i = 0; i < 4; i++) {
            mainMenus.add(String.format("mainMenu%d", i));
        }

        // 테스트용 캠퍼스 메타데이터 생성
        campus1 = new MetaData(Campus.SEOUL);
        campus2 = new MetaData(Campus.GUMI);

        lunch1 = Lunch.builder()
                .id(1L)
                .campus(campus1)
                .mainMenu(mainMenus.get(0))
                .createdAt(today)
                .build();

        lunch2 = Lunch.builder()
                .id(2L)
                .campus(campus1)
                .mainMenu(mainMenus.get(1))
                .createdAt(today)
                .build();

        lunch3 = Lunch.builder()
                .id(3L)
                .campus(campus1)
                .mainMenu(mainMenus.get(2))
                .createdAt(tomorrow)
                .build();

        lunch4 = Lunch.builder()
                .id(4L)
                .campus(campus2)
                .mainMenu(mainMenus.get(3))
                .createdAt(today)
                .build();

        member1 = Member.builder()
                .id(1L)
                .build();
        member2 = Member.builder()
                .id(2L)
                .build();

        lunchPoll1 = LunchPoll.builder()
                .lunch(lunch2)
                .member(member1)
                .polledAt(today)
                .build();

        lunchPoll2 = LunchPoll.builder()
                .lunch(lunch2)
                .member(member2)
                .polledAt(today)
                .build();
    }

    // 점심 메뉴 목록 조회 테스트
    @ParameterizedTest
    @CsvSource({})
    @DisplayName("캠퍼스와 당일 날짜로 점심 메뉴 목록을 조회한다.")
    void Given_TodayDateAndCampus_When_FindLunches_Then_Succeed() {

        // given
        GetLunchListReqDto parameter = GetLunchListReqDto.builder().campus("서울").date(today).build();
        given(metaDataConsumer.getMetaData(MetaDataType.CAMPUS.name(), parameter.getCampus())).willReturn(campus1);
        given(lunchRepository.findAllByCampusAndDate(campus1, today)).willReturn(Optional.of(List.of(lunch2, lunch1)));

        // when
        GetLunchListResDto result = lunchService.findLunches(null, parameter);

        // then
        assertAll(
                () -> assertThat(result.getMenus().get(0))
                        .usingRecursiveComparison()
                        .isEqualTo(GetLunchListElementResDto.of(lunch2,2L)),
                () -> assertThat(result.getMenus().get(1))
                        .usingRecursiveComparison()
                        .isEqualTo(GetLunchListElementResDto.of(lunch1,2L)),
                ()-> assertThat(result.getPolledAt()).isEqualTo(-1)
        );
    }

    @Test
    @DisplayName("캠퍼스와 당일 날짜, 멤버 아이디로 투표 정보가 포함된 점심 메뉴 목록을 조회한다.")
    void Given_TodayDateAndCampusWithMemberId_When_FindLunches_Then_Succeed() {
    }

    @Test
    @DisplayName("캠퍼스와 날짜로 요청시 정보가 없는 점심 메뉴 목록 조회 성공")
    void Given_ValidArgument_When_FindLunchesWithEmptyResult_Then_Succeed() {
    }

    @Test
    @DisplayName("잘못된 타입의 파라미터로 점심 메뉴 목록 조회시 예외를 던진다.")
    void Given_IllegalArgument_When_FindLunches_Then_ThrowException() {
    }

    @Test
    @DisplayName("유효하지 않은 캠퍼스로 점심 메뉴 목록 조회시 예외를 던진다.")
    void Given_InvalidCampus_When_FindingAllLunches_Then_ThrowException() {
    }

    @Test
    @DisplayName("유효하지 않은 날짜의 점심 메뉴 목록 조회시 예외를 던진다.")
    void Given_InvalidDate_When_FindingAllLunches_Then_ThrowException() {
    }

    // 점심 메뉴 상세 조회 테스트
    @Test
    @DisplayName("점심 아이디를 경로로 점심 상세 정보를 조회한다.")
    void Given_ValidLunch_When_FindLunchDetail_Then_Succeed(){
    }

    @Test
    @DisplayName("유효하지 않은 날짜의 점심 아이디의 점심 메뉴 상세 조회 요청 시 예외를 던진다.")
    void Given_InValidDateLunch_When_FindLunchDetail_Then_ThrowException(){
    }

    @Test
    @DisplayName("잘못된 타입의 경로의 점심 메뉴 상세 조회 요청 시 예외를 던진다.")
    void Given_IllegalArgument_When_FindLunchDetail_Then_Succeed(){
    }
}