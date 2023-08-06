package com.ssafy.ssafsound.domain.lunch.service;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.domain.LunchPoll;
import com.ssafy.ssafsound.domain.lunch.dto.PostLunchPollResDto;
import com.ssafy.ssafsound.domain.lunch.exception.LunchErrorInfo;
import com.ssafy.ssafsound.domain.lunch.exception.LunchException;
import com.ssafy.ssafsound.domain.lunch.repository.LunchPollRepository;
import com.ssafy.ssafsound.domain.lunch.repository.LunchRepository;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LunchPollServiceTest {

    @Mock
    private LunchRepository lunchRepository;

    @Mock
    private LunchPollRepository lunchPollRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Clock clock;

    @InjectMocks
    private LunchPollService lunchPollService;

    private Lunch lunch1;
    private Lunch lunch2;
    private Lunch lunch3;

    private Member member1;
    private Member member2;

    private LunchPoll lunchPoll;

    private String testLocalDateTime = "2023-07-10T10:15:00Z";
    private String testLocalDate = "2023-07-10";
    private LocalDate today = LocalDate.parse(testLocalDate);

    @BeforeEach
    void setUp() {

        lunch1 = Lunch.builder()
                .id(1L)
                .createdAt(today)
                .build();

        lunch2 = Lunch.builder()
                .id(2L)
                .createdAt(today)
                .build();

        lunch3 = Lunch.builder()
                .id(3L)
                .createdAt(today.plusDays(1))
                .build();

        member1 = Member.builder()
                .id(1L)
                .build();

        member2 = Member.builder()
                .id(2L)
                .build();

        lunchPoll = LunchPoll.builder()
                .member(member1)
                .polledAt(today)
                .build();

        lunchPoll.setLunch(lunch2);

        // Clock mocking
        given(clock.instant()).willReturn(Instant.parse(testLocalDateTime));
        given(clock.getZone()).willReturn(ZoneId.of("Asia/Seoul"));

        lenient().when(lunchRepository.findById(1L)).thenReturn(Optional.of(lunch1));
        lenient().when(lunchRepository.findById(2L)).thenReturn(Optional.of(lunch2));
        lenient().when(lunchRepository.findById(3L)).thenReturn(Optional.of(lunch3));

        lenient().when(memberRepository.findById(1L)).thenReturn(Optional.of(member1));
        lenient().when(memberRepository.findById(2L)).thenReturn(Optional.of(member2));

        lenient().when(lunchPollRepository.findByMemberAndLunch(member1, lunch2)).thenReturn(Optional.of(lunchPoll));
    }

    @ParameterizedTest
    @CsvSource({"1, 1, 1, 0", "2, 1, 1, 1"})
    @DisplayName("점심 메뉴에 투표한다.")
    void Given_MemberIdAndLunchId_When_SaveLunchPoll_Then_Succeed(Long memberId, Long lunchId,
                                                                  Long expectedLunch1PollCount,
                                                                  Long expectedLunch2PollCount) {

        // given
        lenient().when(lunchPollRepository.findByMemberAndPolledAt(member1, today)).thenReturn(lunchPoll);
        lenient().when(lunchPollRepository.findByMemberAndPolledAt(member2, today)).thenReturn(null);

        // when
        PostLunchPollResDto postLunchPollResDto = lunchPollService.saveLunchPoll(memberId, lunchId);

        // then
        assertAll(
                () -> assertEquals(expectedLunch1PollCount, postLunchPollResDto.getPollCount()),
                () -> assertEquals(expectedLunch2PollCount, lunch2.getLunchPolls().size())
        );

    }

    @Test
    @DisplayName("존재하지 않는 점심 메뉴에 투표하려는 경우 예외를 던진다.")
    void Given_InvalidLunchId_When_SaveLunchPoll_Then_ThrowException() {

        // given
        Long memberId = 1L;
        Long lunchId = 4L;

        // when, then
        LunchException exception = assertThrows(LunchException.class, () -> lunchPollService.saveLunchPoll(memberId, lunchId));
        assertEquals(LunchErrorInfo.INVALID_LUNCH_ID, exception.getInfo());

    }

    @Test
    @DisplayName("존재하지 않는 멤버 아이디로 투표하려는 경우 예외를 던진다.")
    void Given_InvalidMemberId_When_SaveLunchPoll_Then_ThrowException() {

        // given
        Long memberId = 3L;
        Long lunchId = 1L;

        // when, then
        MemberException exception = assertThrows(MemberException.class, () -> lunchPollService.saveLunchPoll(memberId, lunchId));
        assertEquals(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID, exception.getInfo());

    }

    @Test
    @DisplayName("이미 투표한 점심 메뉴에 재투표할 경우 예외를 던진다.")
    void Given_AlreadyPolledLunchId_When_SaveLunchPoll_Then_ThrowException() {

        // given
        Long memberId = 1L;
        Long lunchId = 2L;
        given(lunchPollRepository.findByMemberAndPolledAt(member1, today)).willReturn(lunchPoll);

        // when, then
        LunchException exception = assertThrows(LunchException.class, () -> lunchPollService.saveLunchPoll(memberId, lunchId));
        assertEquals(LunchErrorInfo.DUPLICATE_LUNCH_POLL, exception.getInfo());

        verify(lunchPollRepository).findByMemberAndPolledAt(member1, today);
    }

    @Test
    @DisplayName("당일의 메뉴가 아닌 투표의 경우 예외를 던진다.")
    void Given_NotTodayLunchId_When_SaveLunchPoll_Then_ThrowException() {

        // given
        Long lunchId = 3L;
        Long memberId = 1L;

        // when, then
        LunchException exception = assertThrows(LunchException.class, () -> lunchPollService.saveLunchPoll(memberId, lunchId));
        assertEquals(LunchErrorInfo.INVALID_DATE, exception.getInfo());

    }

    @Test
    @DisplayName("점심 메뉴 투표를 취소한다.")
    void Given_MemberIdAndLunchId_When_DeleteLunchPoll_Then_Succeed() {

        // given
        Long memberId = 1L;
        Long lunchId = 2L;

        // when
        PostLunchPollResDto postLunchPollResDto = lunchPollService.deleteLunchPoll(memberId, lunchId);

        // then
        assertEquals(0, postLunchPollResDto.getPollCount());

    }

    @Test
    @DisplayName("존재하지 않는 점심 메뉴에 대해서 투표를 취소하려는 경우 예외를 던진다.")
    void Given_InvalidLunchId_When_DeleteLunchPoll_Then_ThrowException() {

        // given
        Long memberId = 1L;
        Long lunchId = 4L;

        // when, then
        LunchException exception = assertThrows(LunchException.class, () -> lunchPollService.deleteLunchPoll(memberId, lunchId));
        assertEquals(LunchErrorInfo.INVALID_LUNCH_ID, exception.getInfo());

    }

    @Test
    @DisplayName("존재하지 않는 투표의 경우 예외를 던진다.")
    void Given_NotPolledLunchPoll_When_DeleteLunchPoll_Then_ThrowException() {

        // given
        Long memberId = 2L;
        Long lunchId = 1L;

        // when, then
        LunchException exception = assertThrows(LunchException.class, () -> lunchPollService.deleteLunchPoll(memberId, lunchId));
        assertEquals(LunchErrorInfo.NO_LUNCH_POLL, exception.getInfo());

    }

    @Test
    @DisplayName("당일의 메뉴가 아닌 투표의 경우 예외를 던진다.")
    void Given_NotTodayLunchId_When_DeleteLunchPoll_Then_ThrowException() {

        // given
        Long lunchId = 3L;
        Long memberId = 1L;

        // when, then
        LunchException exception = assertThrows(LunchException.class, () -> lunchPollService.deleteLunchPoll(memberId, lunchId));
        assertEquals(LunchErrorInfo.INVALID_DATE, exception.getInfo());

    }

}