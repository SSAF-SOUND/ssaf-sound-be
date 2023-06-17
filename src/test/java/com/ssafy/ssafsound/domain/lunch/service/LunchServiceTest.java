package com.ssafy.ssafsound.domain.lunch.service;

import com.ssafy.ssafsound.domain.lunch.repository.LunchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LunchServiceTest {

    @Mock
    private LunchRepository lunchRepository;

    @InjectMocks
    private LunchService lunchService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("정상 파라미터 요청시 점심 메뉴 목록 조회 성공")
    void Given_ValidArgument_When_FindingAllLunchesBriefly_Then_Succeed() {
    }

    @Test
    @DisplayName("잘못된 타입의 파라미터 요청시 점심 메뉴 목록 조회 실패")
    void Given_IllegalArgument_When_FindingAllLunchesBriefly_Then_ThrowException() {
    }

    @Test
    @DisplayName("유효하지 않은 캠퍼스 요청시 점심 메뉴 목록 조회 실패")
    void Given_InvalidCampus_When_FindingAllLunchesBriefly_Then_ThrowException() {
    }

    @Test
    @DisplayName("유효하지 않은 날짜 요청시 점심 메뉴 목록 조회 성공")
    void Given_InvalidDate_When_FindingAllLunchesBriefly_Then_ThrowException() {
    }

    @Test
    @DisplayName("정상 경로 요청시 점심 메뉴 상세 조회 성공")
    void Given_ValidPathVariable_When_FindingALunchDetail_Then_Succeed(){
    }

    @Test
    @DisplayName("정상 점심 아이디 요청시 점심 메뉴 상세 조회 성공")
    void Given_ValidId_When_FindingALunchDetail_Then_Succeed(){
    }

    @Test
    @DisplayName("유효하지 않은 점심 아이디 요청시 점심 메뉴 상세 조회 실패")
    void Given_InvalidId_When_FindingALunchDetail_Then_Succeed(){
    }
}