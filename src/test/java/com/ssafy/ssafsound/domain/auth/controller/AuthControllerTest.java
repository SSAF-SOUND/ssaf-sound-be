package com.ssafy.ssafsound.domain.auth.controller;

import com.ssafy.ssafsound.domain.auth.fixture.AuthFixture;
import com.ssafy.ssafsound.domain.auth.service.AuthService;
import com.ssafy.ssafsound.domain.auth.service.CookieProvider;
import com.ssafy.ssafsound.domain.member.service.MemberService;
import com.ssafy.ssafsound.global.docs.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTest {

    @MockBean
    MemberService memberService;
    @SpyBean
    CookieProvider cookieProvider;
    @MockBean
    AuthService authService;

    @DisplayName("oauth 로그인 요청 시, Redirect 를 제공한다.")
    @ParameterizedTest
    @MethodSource("provideOAuthNames")
    void socialLoginRedirect(String oauthName) {
        restDocs
                .when().get("/auth/{oauthName}", oauthName)
                .then().log().all()
                .assertThat()
                .apply(document("oauth/redirect",
                        pathParameters(parameterWithName("oauthName")
                                .description("google, kakao, github, apple 중 하나를 입력하세요"))))
                .expect(status().isOk());
    }

    @DisplayName("oauth login에 성공합니다.")
    @ParameterizedTest
    @MethodSource("provideOAuthNames")
    void socialLoginSuccess(String oauthName) {

        given(authService.login(any())).willReturn(AuthFixture.postMemberReqDto);
        given(memberService.createMemberByOauthIdentifier(any())).willReturn(AuthFixture.authenticatedMember);
        given(authService.createToken(any())).willReturn(AuthFixture.createMemberTokensResDto);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(AuthFixture.getCreateMemberReqDto(oauthName))
                .when().post("/auth/callback")
                .then().log().all()
                .assertThat()
                .apply(document("oauth/login",
                        getEnvelopPattern()
                                .and(fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"))
                                .and(fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰"))
                ))
                .expect(status().isOk());
    }

    @DisplayName("로그아웃 요청 시, 로그아웃에 성공합니다.")
    @Test
    void logoutWithCookie() {
        restDocs
                .cookie(ACCESS_TOKEN)
                .cookie(REFRESH_TOKEN)
                .when().delete("/auth/logout")
                .then().log().all()
                .apply(document("oauth/logout/ExistCookie"))
                .expect(status().isOk());
    }

    @DisplayName("로그아웃 요청 시 쿠키가 없다면 바로 로그아웃에 성공합니다.")
    @Test
    void logoutWithNotCookie() {
        restDocs
                .when().delete("/auth/logout")
                .then().log().all()
                .apply(document("oauth/logout/notExistCookie"))
                .expect(status().isOk());
    }

    @DisplayName("토큰 재발급 요청에 성공합니다.")
    @Test
    void reissueByRefreshToken() {

        given(authService.getMemberTokenByRefreshToken(any()))
                .willReturn(AuthFixture.memberToken);

        given(authService.reissueAccessToken(any()))
                .willReturn(AuthFixture.createMemberAccessTokenResDto);

        given(cookieProvider
                .setCookieWithOptions("accessToken", AuthFixture.createMemberAccessTokenResDto.getAccessToken()))
                .willReturn(AuthFixture.getAccessTokenCookie());

        restDocs
                .cookie(REFRESH_TOKEN)
                .when().get("/auth/reissue")
                .then().log().all()
                .assertThat()
                .apply(document("oauth/reissue",
                        getEnvelopPattern()
                                .and(fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"))))
                .expect(status().isOk());
    }


    static Stream<String> provideOAuthNames() {
        return Stream.of("google", "github", "kakao");
    }
}