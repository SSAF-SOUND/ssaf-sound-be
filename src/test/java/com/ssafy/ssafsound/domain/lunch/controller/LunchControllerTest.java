package com.ssafy.ssafsound.domain.lunch.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListElementResDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListReqDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListResDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchResDto;
import com.ssafy.ssafsound.domain.lunch.service.LunchPollService;
import com.ssafy.ssafsound.domain.lunch.service.LunchService;
import com.ssafy.ssafsound.domain.meta.domain.Campus;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.global.docs.RestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LunchController.class)
public class LunchControllerTest extends RestDocsTest {

    @MockBean
    private LunchService lunchService;

    @MockBean
    private LunchPollService lunchPollService;

    @MockBean
    GetLunchListReqDto getLunchListReqDto;

    @Test
    @DisplayName("캠퍼스, 날짜로 점심 메뉴 목록 조회")
    public void getLunchesByCampusAndDate() throws Exception {

        // 요청
        Cookie cookie = new Cookie("accessToken", "{accessToken}");

        LocalDate requestParamDate = LocalDate.now();
        String requestParamCampus = Campus.SEOUL.getName();

        getLunchListReqDto = GetLunchListReqDto.builder()
                .date(requestParamDate)
                .campus(requestParamCampus)
                .build();


        // 응답
        List<GetLunchListElementResDto> getLunchListElementResDtos = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            getLunchListElementResDtos.add(GetLunchListElementResDto.of(Lunch.builder()
                    .id((long)i)
                    .campus(new MetaData(Campus.SEOUL))
                    .createdAt(requestParamDate)
                    .mainMenu("메인메뉴" + i)
                    .imagePath("www.sample" + i + ".png")
                    .build(), (long) (30 - i * i * 2)));
        }

        GetLunchListResDto getLunchListResDto = GetLunchListResDto.of(getLunchListElementResDtos, 2L);

        // mocking
        given(authenticationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(AuthenticatedMember.builder().memberId(1L).build());

        given(lunchService.findLunches(any(), any())).willReturn(getLunchListResDto);

        // controller test
        mockMvc.perform(get("/lunch?date=2023-08-12&campus=서울")
                        .cookie(cookie)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestParameters(
                                        parameterWithName("date").description("조회하려는 yyyy-MM-DD 형식 일자. 당일과 익일만 가능합니다."),
                                        parameterWithName("campus").description("조회하려는 캠퍼스 한글명. (서울 | 부울경 | 구미 | 광주 )( 대전은 현재 미지원 )")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.totalPollCount").type(JsonFieldType.NUMBER).description("전체 메뉴의 투표수 합계"),
                                        fieldWithPath("data.polledAt").type(JsonFieldType.NUMBER).description("점심 메뉴 목록에서 인증된 사용자의 투표 선택지 인덱스. 미인증 혹은 투표하지 않았을 경우 -1"),
                                        fieldWithPath("data.menus").type(JsonFieldType.ARRAY).description("점심 메뉴 목록")
                                ).andWithPrefix("data.menus[]",
                                        fieldWithPath("mainMenu").type(JsonFieldType.STRING).description("메인 메뉴명"),
                                        fieldWithPath("imagePath").type(JsonFieldType.STRING).description("메인 메뉴 이미지 url"),
                                        fieldWithPath("pollCount").type(JsonFieldType.NUMBER).description("메뉴 투표수")
                                )
                        )
                );
    }

    @Test
    @DisplayName("점심 메뉴 상세 조회")
    public void getLunchByLunchId() throws Exception {

        Long sampleLunchId = 1L;

        Lunch lunch = Lunch.builder()
                .id(sampleLunchId)
                .mainMenu("메인 메뉴1")
                .extraMenu("메인 메뉴1, 엑스트라 메뉴1, 엑스트라 메뉴2")
                .sumKcal("2,321 kcal")
                .build();

        given(lunchService.findLunchDetail(sampleLunchId)).willReturn(GetLunchResDto.of(lunch));

        this.mockMvc.perform(get("/lunch/{lunchId}", sampleLunchId))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("lunchId").description("점심 메뉴 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.mainMenu").type(JsonFieldType.STRING).description("메인 메뉴명"),
                                        fieldWithPath("data.extraMenu").type(JsonFieldType.STRING).description("전체 메뉴명"),
                                        fieldWithPath("data.sumKcal").type(JsonFieldType.STRING).description("총 칼로리")
                                )
                        )
                );
    }

}
