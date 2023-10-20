package com.ssafy.ssafsound.domain.member.controller;

import com.ssafy.ssafsound.domain.member.dto.GetMemberDefaultInfoResDto;
import com.ssafy.ssafsound.domain.member.dto.GetMemberResDto;
import com.ssafy.ssafsound.domain.member.dto.PostMemberInfoReqDto;
import com.ssafy.ssafsound.global.docs.ControllerTest;
import com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet;
import com.ssafy.ssafsound.global.util.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import java.util.HashSet;
import java.util.Set;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenMandatory;
import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenNeedless;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTest {

    private final MemberFixture memberFixture = new MemberFixture();

    public ResponseFieldsSnippet getPortfolioSnippet() {
        return getEnvelopPatternWithData()
                .andWithPrefix("data.portfolioElement.",
                        fieldWithPath("selfIntroduction").type(JsonFieldType.STRING)
                                .description("멤버 소개 글"),
                        fieldWithPath("skills").type(JsonFieldType.ARRAY)
                                .description("멤버 기술 스택들"),
                        fieldWithPath("memberLinks")
                                .type(JsonFieldType.ARRAY)
                                .description("멤버 소개 링크들"),
                        fieldWithPath("memberLinks[].linkName")
                                .type(JsonFieldType.STRING)
                                .description("멤버의 소개 링크 이름"),
                        fieldWithPath("memberLinks[].path")
                                .type(JsonFieldType.STRING)
                                .description("멤버의 소개 링크 경로"));
    }

    public RequestFieldsSnippet requestSSAFYSnippet() {
        return requestFields(
                fieldWithPath("nickname").description("닉네임"),
                fieldWithPath("ssafyMember").description("싸피인 여부"),
                fieldWithPath("isMajor").description("전공자 여부"),
                fieldWithPath("semester").optional().description("싸피 기수"),
                fieldWithPath("campus").optional().description("캠퍼스 이름"),
                fieldWithPath("termIds").description("필수 약관 동의 아이디 값")
        );
    }

    public ResponseFieldsSnippet getMemberSnippet() {
        return getEnvelopPatternWithData()
                .and(fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("멤버 ID"))
                .and(fieldWithPath("data.memberRole").type(JsonFieldType.STRING).description("멤버 권한"))
                .and(fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("닉네임").optional())
                .and(fieldWithPath("data.ssafyMember").type(JsonFieldType.BOOLEAN).description("싸피인 여부").optional())
                .and(fieldWithPath("data.isMajor").type(JsonFieldType.BOOLEAN).description("전공자 여부").optional())
                .and(fieldWithPath("data.ssafyInfo").type(JsonFieldType.OBJECT)
                        .description("싸피인 정보").optional())
                .andWithPrefix("data.ssafyInfo.",
                        fieldWithPath("semester").type(JsonFieldType.NUMBER)
                                .description("싸피 기수 정보").optional(),
                        fieldWithPath("campus").type(JsonFieldType.STRING)
                                .description("캠퍼스 이름").optional(),
                        fieldWithPath("certificationState").type(JsonFieldType.STRING)
                                .description("싸피생 인증상태(CERTIFIED/UNCERTIFIED)").optional(),
                        fieldWithPath("majorTrack").type(JsonFieldType.STRING)
                                .description("전공 트랙(인증상태가 UNCERTIFIED라면 NULL)").optional());
    }

    @DisplayName("회원가입 후 처음으로 멤버 본인정보 요청 시, 조회에 성공한다.")
    @Test
    void getMemberInformationByFirstTry() {

        given(memberService.getMemberInformation(any()))
                .willReturn(GetMemberResDto.fromGeneralUser(memberFixture.createInitializerMember()));

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
                .willReturn(GetMemberResDto.fromGeneralUser(memberFixture.createGeneralMember()));

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
                                        .description("싸피인 여부"))
                                .and(fieldWithPath("data.isMajor").type(JsonFieldType.BOOLEAN)
                                        .description("전공자 여부"))
                                .and(fieldWithPath("data.ssafyInfo").type(JsonFieldType.NULL)
                                        .description("싸피인 정보 NULL"))))
                .expect(status().isOk());
    }

    @DisplayName("싸피멤버 본인정보 요청 시, 조회에 성공한다.")
    @Test
    void getSSAFYMemberInformation() {

        given(memberService.getMemberInformation(any()))
                .willReturn(memberFixture.createCertifiedSSAFYMemberResDto());

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().get("/members")
                .then().log().all()
                .assertThat()
                .apply(document("members/ssafy-information",
                        requestCookieAccessTokenMandatory(),
                        getMemberSnippet()))
                .expect(status().isOk());
    }

    @DisplayName("사용자 회원가입 플로우에서 기본 정보 입력에 성공한다.")
    @Test
    void putMemberInformation() {

        given(memberService.registerMemberInformation(any(), any()))
                .willReturn(GetMemberResDto.fromGeneralUser(memberFixture.createGeneralMember()));
        given(semesterValidator.isValid(any(), any())).willReturn(true);

        PostMemberInfoReqDto postMemberInfoReqDto = PostMemberInfoReqDto.builder()
                .nickname("james")
                .ssafyMember(false)
                .isMajor(true)
                .termIds(new HashSet<>(Set.of(1L, 2L, 3L)))
                .build();

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(ACCESS_TOKEN)
                .body(postMemberInfoReqDto)
                .when().put("/members")
                .then().log().all()
                .assertThat()
                .apply(document("members/put-information",
                        requestCookieAccessTokenMandatory(),
                        requestSSAFYSnippet(),
                        getMemberSnippet()))
                .expect(status().isOk());
    }

    @DisplayName("사용자 회원가입 플로우에서 싸피 정보 입력에 성공한다.")
    @Test
    void putMemberSSAFYInformation() {

        given(memberService.registerMemberInformation(any(), any()))
                .willReturn(memberFixture.createUncertifiedSSAFYMemberResDto());
        given(semesterValidator.isValid(any(), any())).willReturn(true);
        given(semesterConstantProvider.getMAX_SEMESTER()).willReturn(10);
        given(semesterConstantProvider.getMIN_SEMESTER()).willReturn(1);

        PostMemberInfoReqDto postMemberInfoReqDto = PostMemberInfoReqDto.builder()
                .nickname("james")
                .ssafyMember(true)
                .isMajor(true)
                .semester(9)
                .campus("서울")
                .build();

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(ACCESS_TOKEN)
                .body(postMemberInfoReqDto)
                .when().put("/members")
                .then().log().all()
                .assertThat()
                .apply(document("members/put-ssafy-information",
                        requestCookieAccessTokenMandatory(),
                        requestSSAFYSnippet(),
                        getMemberSnippet()))
                .expect(status().isOk());
    }

    @DisplayName("싸피생 인증에 대해서 성공한다.")
    @Test
    void certificationSSAFY() {

        given(memberService.certifySSAFYInformation(any(), any()))
                .willReturn(memberFixture.createPostCertificationInfoResDto());
        given(semesterValidator.isValid(any(), any())).willReturn(true);
        given(semesterConstantProvider.getMAX_SEMESTER()).willReturn(10);
        given(semesterConstantProvider.getMIN_SEMESTER()).willReturn(1);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(ACCESS_TOKEN)
                .body(memberFixture.createPostCertificationInfoReqDto())
                .when().post("/members/ssafy-certification")
                .then().log().all()
                .assertThat()
                .apply(document("members/ssafy-certification",
                        requestCookieAccessTokenMandatory(),
                        requestFields(
                                fieldWithPath("majorTrack").description("전공트랙"),
                                fieldWithPath("semester").description("기수"),
                                fieldWithPath("answer").description("기수에 따른 출제 문제에 대한 정답")),
                        getEnvelopPatternWithData()
                                .and(fieldWithPath("data.possible").type(JsonFieldType.BOOLEAN)
                                        .description("인증 상태 여부"))
                                .and(fieldWithPath("data.certificationInquiryCount").type(JsonFieldType.NUMBER)
                                        .description("인증 시도 횟수"))))
                .expect(status().isOk());
    }

    @DisplayName("나의 포트폴리오 조회에 성공한다.")
    @Test
    void getMyPortfolio() {

        given(memberService.getMyPortfolio(any()))
                .willReturn(memberFixture.createGetMemberPortfolioResDto());

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().get("/members/portfolio")
                .then().log().all()
                .assertThat()
                .apply(document("members/get-portfolio",
                        requestCookieAccessTokenMandatory(),
                        getPortfolioSnippet()))
                .expect(status().isOk());
    }

    @DisplayName("다른 멤버의 포트폴리오 가져오기에 성공한다.")
    @Test
    void getOtherPortfolio() {

        given(memberService.getMemberPortfolioById(any()))
                .willReturn(memberFixture.createGetMemberPortfolioResDto());

        restDocs
                .when().get("members/{memberId}/portfolio", 99)
                .then().log().all()
                .assertThat()
                .apply(document("members/get-other-portfolio",
                        CookieDescriptionSnippet.requestCookieAccessTokenNeedless(),
                        pathParameters(parameterWithName("memberId")
                                .description("멤버 Id")),
                        getPortfolioSnippet()))
                .expect(status().isOk());
    }

    @DisplayName("포트폴리오 수정에 성공한다.")
    @Test
    void putMemberPortfolio() {
        restDocs
                .cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberFixture.createPutMemberPortfolioReqDto())
                .when().put("members/portfolio")
                .then().log().all()
                .assertThat()
                .apply(document("members/put-member-portfolio",
                        requestCookieAccessTokenMandatory(),
                        requestFields(
                                fieldWithPath("selfIntroduction").description("자기소개"),
                                fieldWithPath("skills[]").description("기술 스택들").optional(),
                                fieldWithPath("memberLinks[]").description("소개 링크들").optional(),
                                fieldWithPath("memberLinks[].linkName").description("링크 이름"),
                                fieldWithPath("memberLinks[].path").description("링크 경로")),
                        getEnvelopPatternWithNoContent()))
                .expect(status().isOk());
    }

    @DisplayName("멤버의 기본 정보 조회에 성공한다.")
    @Test
    void getMemberDefaultInformation() {

        given(memberService.getMemberDefaultInfoByMemberId(any()))
                .willReturn(GetMemberDefaultInfoResDto.from(memberFixture.createMember()));

        restDocs
                .when().get("members/{memberId}/default-information", 99)
                .then().log().all()
                .assertThat()
                .apply(document("members/get-default-information",
                        CookieDescriptionSnippet.requestCookieAccessTokenNeedless(),
                        pathParameters(parameterWithName("memberId").description("멤버 Id")),
                        getEnvelopPatternWithData()
                                .and(fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("닉네임").optional())
                                .and(fieldWithPath("data.ssafyMember").type(JsonFieldType.BOOLEAN).description("싸피인 여부").optional())
                                .and(fieldWithPath("data.isMajor").type(JsonFieldType.BOOLEAN).description("전공자 여부").optional())
                                .and(fieldWithPath("data.ssafyInfo").type(JsonFieldType.OBJECT)
                                        .description("싸피인 정보").optional())
                                .andWithPrefix("data.ssafyInfo.",
                                        fieldWithPath("semester").type(JsonFieldType.NUMBER)
                                                .description("싸피 기수 정보").optional(),
                                        fieldWithPath("campus").type(JsonFieldType.STRING)
                                                .description("캠퍼스 이름").optional(),
                                        fieldWithPath("certificationState").type(JsonFieldType.STRING)
                                                .description("싸피생 인증상태(CERTIFIED/UNCERTIFIED)").optional(),
                                        fieldWithPath("majorTrack").type(JsonFieldType.STRING)
                                                .description("전공 트랙(인증상태가 UNCERTIFIED라면 NULL)").optional())
                ))
                .expect(status().isOk());
    }

    @DisplayName("멤버의 기본 정보 수정에 대해 성공한다.")
    @Test
    void patchMemberDefaultInformation() {
        restDocs
                .cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberFixture.createPatchMemberDefaultInfoReqDto())
                .when().patch("members/default-information")
                .then().log().all()
                .assertThat()
                .apply(document("members/change-default-information",
                        requestCookieAccessTokenMandatory(),
                        requestFields(
                                fieldWithPath("ssafyMember").description("싸피인 여부"),
                                fieldWithPath("semester").optional().description("기수"),
                                fieldWithPath("campus").optional().description("캠퍼스")),
                        getEnvelopPatternWithNoContent()))
                .expect(status().isOk());
    }

    @DisplayName("나의 프로필 공개 여부 조회에 성공한다.")
    @Test
    void getStatusOfPublicProfile() {

        given(memberService.getMemberPublicProfileByMemberId(any()))
                .willReturn(memberFixture.createGetMemberPublicProfileResDto());

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().get("members/public-profile")
                .then().log().all()
                .assertThat()
                .apply(document("members/get-public",
                        requestCookieAccessTokenMandatory(),
                        getEnvelopPatternWithData()
                                .and(fieldWithPath("data.isPublic").type(JsonFieldType.BOOLEAN)
                                        .description("프로필 공개 여부").optional())))
                .expect(status().isOk());

    }

    @DisplayName("다른 멤버의 프로필 공개 여부 조회에 성공한다.")
    @Test
    void getOtherStatusOfPublicProfile() {

        given(memberService.getMemberPublicProfileByMemberId(any()))
                .willReturn(memberFixture.createGetMemberPublicProfileResDto());

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().get("members/{memberId}/public-profile", 99)
                .then().log().all()
                .assertThat()
                .apply(document("members/get-other-public",
                        requestCookieAccessTokenNeedless(),
                        pathParameters(parameterWithName("memberId").description("멤버 Id")),
                        getEnvelopPatternWithData()
                                .and(fieldWithPath("data.isPublic").type(JsonFieldType.BOOLEAN)
                                        .description("프로필 공개 여부"))))
                .expect(status().isOk());

    }

    @DisplayName("프로필 공개여부 수정에 성공합니다.")
    @Test
    void changePublicProfile() {

        restDocs
                .cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberFixture.createPatchMemberPublicProfileReqDto())
                .when().patch("members/public-profile")
                .then().log().all()
                .assertThat()
                .apply(document("members/change-public-profile",
                        requestCookieAccessTokenMandatory(),
                        requestFields(
                                fieldWithPath("isPublic").description("프로필 공개 여부 수정")),
                        getEnvelopPatternWithNoContent()))
                .expect(status().isOk());
    }

    @DisplayName("닉네임 중복 여부를 검사한다.")
    @Test
    void checkNicknamePossible() {

        given(memberService.checkNicknamePossible(any()))
                .willReturn(memberFixture.createPostNicknameResDto());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberFixture.createPostNicknameReqDto())
                .when().post("members/nickname")
                .then().log().all()
                .assertThat()
                .apply(document("members/check-nickname",
                        requestCookieAccessTokenNeedless(),
                        requestFields(
                                fieldWithPath("nickname").description("닉네임")),
                        getEnvelopPatternWithData()
                                .and(fieldWithPath("data.possible").type(JsonFieldType.BOOLEAN)
                                        .description("닉네임 사용 가능 여부"))))
                .expect(status().isOk());

    }


    @DisplayName("닉네임 변경에 성공한다.")
    @Test
    void changeNickname() {

        restDocs
                .cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberFixture.createPostNicknameReqDto())
                .when().patch("members/nickname")
                .then().log().all()
                .assertThat()
                .apply(document("members/change-nickname",
                        requestCookieAccessTokenMandatory(),
                        requestFields(
                                fieldWithPath("nickname").description("닉네임")),
                        getEnvelopPatternWithNoContent()))
                .expect(status().isOk());
    }

    @DisplayName("전공자 여부에 대한 변경에 성공한다.")
    @Test
    void changeMemberMajor() {

        restDocs
                .cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberFixture.createPatchMemberMajorReqDto())
                .when().patch("members/major")
                .then().log().all()
                .assertThat()
                .apply(document("members/change-isMajor",
                        requestCookieAccessTokenMandatory(),
                        requestFields(
                                fieldWithPath("isMajor").description("전공자유무")),
                        getEnvelopPatternWithNoContent()))
                .expect(status().isOk());
    }

    @DisplayName("전공트랙에 대한 변경에 성공한다.")
    @Test
    void changeMemberMajorTrack() {

        restDocs
                .cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberFixture.createPatchMemberMajorTrackReqDto())
                .when().patch("members/major-track")
                .then().log().all()
                .assertThat()
                .apply(document("members/change-majorTrack",
                        requestCookieAccessTokenMandatory(),
                        requestFields(
                                fieldWithPath("majorTrack")
                                        .description("전공트랙(\"Embedded\" , \"Python\" , \"Java\" , \"Mobile\")")),
                        getEnvelopPatternWithNoContent()))
                .expect(status().isOk());
    }

    @DisplayName("회원 탈퇴 요청에 대해 성공한다.")
    @Test
    void leaveMember() {

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().delete("/members")
                .then().log().all()
                .assertThat()
                .apply(document("members/leave",
                        requestCookieAccessTokenMandatory(),
                        getEnvelopPatternWithNoContent()))
                .expect(status().isOk());
    }
}