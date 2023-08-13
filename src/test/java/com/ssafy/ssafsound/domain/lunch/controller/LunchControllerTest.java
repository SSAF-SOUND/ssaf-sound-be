package com.ssafy.ssafsound.domain.lunch.controller;

import com.ssafy.ssafsound.global.docs.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenMandatory;
import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenNeedless;
import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenOptional;
import static com.ssafy.ssafsound.global.util.fixture.LunchFixture.GET_LUNCH_LIST_RES_DTO;
import static com.ssafy.ssafsound.global.util.fixture.LunchFixture.GET_LUNCH_RES_DTO;
import static com.ssafy.ssafsound.global.util.fixture.LunchFixture.POST_LUNCH_POLL_RES_DTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

@WebMvcTest(LunchController.class)
public class LunchControllerTest extends ControllerTest {

    @Test
    @DisplayName("캠퍼스, 날짜로 점심 메뉴 목록 조회")
    public void getLunchesByCampusAndDate() {
        doReturn(GET_LUNCH_LIST_RES_DTO)
                .when(lunchService)
                .findLunches(any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/lunch?campus=서울&date=2023-08-12")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("lunch/list",
                        requestCookieAccessTokenOptional(),
                        requestParameters(
                                parameterWithName("date").description("조회하려는 yyyy-MM-DD 형식 일자. 당일과 익일만 가능합니다."),
                                parameterWithName("campus").description("조회하려는 캠퍼스 한글명. (서울 | 부울경 | 구미 | 광주 )( 대전은 현재 미지원 )")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("totalPollCount").type(JsonFieldType.NUMBER).description("전체 메뉴의 투표수 합계"),
                                fieldWithPath("polledAt").type(JsonFieldType.NUMBER).description("점심 메뉴 목록에서 인증된 사용자의 투표 선택지 인덱스. 미인증 혹은 투표하지 않았을 경우 -1"),
                                fieldWithPath("menus").type(JsonFieldType.ARRAY).description("점심 메뉴 목록")
                        ).andWithPrefix("data.menus[].",
                                fieldWithPath("mainMenu").type(JsonFieldType.STRING).description("메인 메뉴명"),
                                fieldWithPath("imagePath").type(JsonFieldType.STRING).description("메인 메뉴 이미지 주소"),
                                fieldWithPath("pollCount").type(JsonFieldType.NUMBER).description("메뉴 투표수"))));
    }

    @Test
    @DisplayName("점심 메뉴 상세 조회")
    public void getLunchByLunchId() {

        Long sampleLunchId = 1L;

        doReturn(GET_LUNCH_RES_DTO)
                .when(lunchService)
                .findLunchDetail(any());

        restDocs.when().get("/lunch/{lunchId}", sampleLunchId)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("lunch/detail",
                        requestCookieAccessTokenNeedless(),
                        pathParameters(
                                parameterWithName("lunchId").description("점심 메뉴 아이디")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("mainMenu").type(JsonFieldType.STRING).description("메인 메뉴명"),
                                fieldWithPath("extraMenu").type(JsonFieldType.STRING).description("전체 메뉴명"),
                                fieldWithPath("sumKcal").type(JsonFieldType.STRING).description("총 칼로리"))));
    }

    @Test
    @DisplayName("점심 투표하기")
    public void postLunchPoll() {
        doReturn(POST_LUNCH_POLL_RES_DTO)
                .when(lunchPollService)
                .saveLunchPoll(any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().post("/lunch/poll/{lunchId}", 2L)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("lunch/poll",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("lunchId").description("점심 메뉴 아이디")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("pollCount").type(JsonFieldType.NUMBER).description("메뉴 투표수"))));
    }

    @Test
    @DisplayName("점심 투표 취소하기")
    public void revertLunchPoll() {
        doReturn(POST_LUNCH_POLL_RES_DTO)
                .when(lunchPollService)
                .deleteLunchPoll(any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().post("/lunch/poll/revert/{lunchId}", 2L)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("lunch/poll/revert",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("lunchId").description("점심 메뉴 아이디")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("pollCount").type(JsonFieldType.NUMBER).description("메뉴 투표수"))));
    }

}
