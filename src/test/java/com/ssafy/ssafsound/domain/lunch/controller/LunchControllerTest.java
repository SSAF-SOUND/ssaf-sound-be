package com.ssafy.ssafsound.domain.lunch.controller;

import com.ssafy.ssafsound.domain.meta.domain.Campus;
import com.ssafy.ssafsound.utils.BaseRestDocControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LunchControllerTest extends BaseRestDocControllerTest{

    @Test
    @DisplayName("캠퍼스, 날짜로 점심 메뉴 목록 조회")
    public void getLunchesByCampusAndDate() throws Exception {

        String today = LocalDate.now().toString();
        String campus = Campus.SEOUL.getName();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("date", today);
        params.add("campus", campus);

        FieldDescriptor[] envelopFields = this.getEnvelopeResponseFields();

        this.mockMvc.perform(get("/lunch")
                        .params(params))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestParameters(
                                        parameterWithName("date").description("조회하려는 점심 메뉴의 yyyy-MM-DD 형식 날짜. 당일과 익일만 가능합니다."),
                                        parameterWithName("campus").description("조회하려는 점심 메뉴의 캠퍼스 한글명. (서울 | 부울경 | 구미 | 광주")
                                ),
                                responseFields(
                                        envelopFields[0],
                                        envelopFields[1],
                                        envelopFields[2],
                                        fieldWithPath("data.totalPollCount").description("전체 메뉴의 투표수 합계"),
                                        fieldWithPath("data.polledAt").description("점심 메뉴 목록에서 인증된 사용자의 투표 선택지 인덱스. 미인증 혹은 투표하지 않았을 경우 -1"),
                                        fieldWithPath("data.menus").description("점심 메뉴 목록")
                                ).andWithPrefix("data.menus[]",
                                        fieldWithPath("mainMenu").description("메인 메뉴명"),
                                        fieldWithPath("imagePath").description("메인 메뉴 이미지 url"),
                                        fieldWithPath("pollCount").description("메뉴 투표수")
                                )
                        )
                );

    }

    @Test
    @DisplayName("점심 메뉴 상세 조회")
    public void getLunchByLunchId() throws Exception {

        Long sampleLunchId = 1L;

        this.mockMvc.perform(get("/lunch/{lunchId}", sampleLunchId))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("lunchId").description("점심 메뉴 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        fieldWithPath("data").description("응답 데이터"),
                                        fieldWithPath("data.mainMenu").description("메인 메뉴명"),
                                        fieldWithPath("data.extraMenu").description("전체 메뉴명"),
                                        fieldWithPath("data.sumKcal").description("총 칼로리")
                                )
                        )
                );
    }

//    @Test

}
