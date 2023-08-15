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

    @DisplayName("회원가입 후 처음으로 멤버 본인정보 요청 시, 조회에 성공한다.")
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
                                .and(fieldWithPath("data.ssafyInfo").type(JsonFieldType.NULL).description("싸피인 정보"))))
                .expect(status().isOk());
    }

    @DisplayName("일반멤버 본인정보 요청 시, 조회에 성공한다.")
    @Test
    void getGeneralMemberInformation() {

        given(memberService.getMemberInformation(any()))
                .willReturn(GetMemberResDto.fromGeneralUser(MemberFixture.GENERAL_MEMBER));

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().get("/members")
                .then().log().all()
                .assertThat()
                .apply(document("members/general-information",
                        requestCookieAccessTokenMandatory(),
                        getEnvelopPatternWithData()
                                .and(fieldWithPath("data.memberId").type(JsonFieldType.NUMBER)
                                        .description("멤버 ID"))
                                .and(fieldWithPath("data.memberRole").type(JsonFieldType.STRING)
                                        .description("멤버 권한"))
                                .and(fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                        .description("닉네임"))
                                .and(fieldWithPath("data.ssafyMember").type(JsonFieldType.BOOLEAN)
                                        .description("싸피인 여부 FALSE"))
                                .and(fieldWithPath("data.isMajor").type(JsonFieldType.BOOLEAN)
                                        .description("전공자 여부 TRUE OR FALSE"))
                                .and(fieldWithPath("data.ssafyInfo").type(JsonFieldType.NULL)
                                        .description("싸피인 정보 NULL"))))
                .expect(status().isOk());
    }

    @DisplayName("싸피멤버 본인정보 요청 시, 조회에 성공한다.")
    @Test
    void getSSAFYMemberInformation() {

        given(memberService.getMemberInformation(any()))
                .willReturn(MemberFixture.SSAFY_MEMBER_RESPONSE_DTO);

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().get("/members")
                .then().log().all()
                .assertThat()
                .apply(document("members/ssafy-information",
                        requestCookieAccessTokenMandatory(),
                        getEnvelopPatternWithData()
                                .and(fieldWithPath("data.memberId").type(JsonFieldType.NUMBER)
                                        .description("멤버 ID"))
                                .and(fieldWithPath("data.memberRole").type(JsonFieldType.STRING)
                                        .description("멤버 권한"))
                                .and(fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                        .description("닉네임"))
                                .and(fieldWithPath("data.ssafyMember").type(JsonFieldType.BOOLEAN)
                                        .description("싸피인 여부 FALSE"))
                                .and(fieldWithPath("data.isMajor").type(JsonFieldType.BOOLEAN)
                                        .description("전공자 여부 TRUE OR FALSE"))
                                .and(fieldWithPath("data.ssafyInfo").type(JsonFieldType.OBJECT)
                                        .description("싸피인 정보"))
                                .andWithPrefix("data.ssafyInfo.",
                                        fieldWithPath("semester").type(JsonFieldType.NUMBER)
                                                .description("싸피 기수 정보"),
                                        fieldWithPath("campus").type(JsonFieldType.STRING)
                                                .description("캠퍼스 이름"),
                                        fieldWithPath("certificationState").type(JsonFieldType.STRING)
                                                .description("싸피생 인증상태(CERTIFIED/UNCERTIFIED)"),
                                        fieldWithPath("majorTrack").type(JsonFieldType.STRING)
                                                .description("전공 트랙(인증상태가 UNCERTIFIED라면 NULL)"))))
                .expect(status().isOk());
    }
}