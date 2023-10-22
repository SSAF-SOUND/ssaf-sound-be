package com.ssafy.ssafsound.domain.term.controller;

import com.ssafy.ssafsound.domain.term.dto.GetTermsResDto;
import com.ssafy.ssafsound.global.docs.ControllerTest;
import com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet;
import com.ssafy.ssafsound.global.util.fixture.TermFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TermControllerTest extends ControllerTest {

    private final TermFixture termFixture = new TermFixture();


    public ResponseFieldsSnippet getTermSnippet() {
        return getEnvelopPatternWithData()
                .andWithPrefix("data.termElements[]",
                        fieldWithPath("termId").type(JsonFieldType.NUMBER)
                                .description("약관 아이디"),
                        fieldWithPath("termName").type(JsonFieldType.STRING)
                                .description("약관 이름"),
                        fieldWithPath("content")
                                .type(JsonFieldType.STRING)
                                .description("약관 내용"),
                        fieldWithPath("required")
                                .type(JsonFieldType.BOOLEAN)
                                .description("필수 여부"),
                        fieldWithPath("sequence")
                                .type(JsonFieldType.NUMBER)
                                .description("약관 배치 순서"));
    }

    @DisplayName("사용중인 약관들을 리스트들을 가져온다.")
    @Test
    void getUsedTerms() {
        given(termService.getTerms())
                .willReturn(GetTermsResDto.fromTermElements(termFixture.createTermElements()));

        restDocs
                .when().get("/terms")
                .then().log().all()
                .assertThat()
                .apply(document("terms/get-required-terms",
                        CookieDescriptionSnippet.requestCookieAccessTokenNeedless(),
                        getTermSnippet()
                )).expect(status().isOk());
    }
}