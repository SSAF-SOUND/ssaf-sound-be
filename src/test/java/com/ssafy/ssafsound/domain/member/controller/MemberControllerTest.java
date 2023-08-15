package com.ssafy.ssafsound.domain.member.controller;

import com.ssafy.ssafsound.domain.member.dto.GetMemberResDto;
import com.ssafy.ssafsound.global.docs.ControllerTest;
import com.ssafy.ssafsound.global.util.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenMandatory;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTest {

    @DisplayName("회원가입 후 처음 멤버 정보 요청 시, 조회에 성공한다.")
    @Test
    void getMemberInformationByFirstTry() {

        given(memberService.getMemberInformation(any()))
                .willReturn(GetMemberResDto.fromGeneralUser(MemberFixture.INITIALIZER_MEMBER));

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().get("/members")
                .then().log().all()
                .assertThat()
                .apply(document("members/information",
                        requestCookieAccessTokenMandatory(),
                        getEnvelopPatternWithData()
                                .and(fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("멤버 ID"))
                                .and(fieldWithPath("data.memberRole").type(JsonFieldType.STRING).description("멤버 권한"))
                                .and(fieldWithPath("data.nickname").type(JsonFieldType.NULL).description("닉네임"))
                                .and(fieldWithPath("data.ssafyMember").type(JsonFieldType.NULL).description("싸피인 여부"))
                                .and(fieldWithPath("data.isMajor").type(JsonFieldType.NULL).description("전공자 여부"))
                                .and(fieldWithPath("data.ssafyInfo").type(JsonFieldType.NULL).description("싸피인 여부"))))
                .expect(status().isOk());


    }
}