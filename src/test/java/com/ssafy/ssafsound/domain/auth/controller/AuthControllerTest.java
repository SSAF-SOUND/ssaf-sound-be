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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
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
    void socialLoginRedirect(String oauthName) throws Exception {
        this.mockMvc.perform(
                get("/auth/{oauthName}", oauthName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(pathParameters(
                        parameterWithName("oauthName").description("google, kakao, github, apple 중 하나를 입력하세요")
                )));
    }

    @DisplayName("oauth login에 성공합니다.")
    @Test
    void socialLoginSuccess() throws Exception {

        String content = objectMapper.writeValueAsString(AuthFixture.createMemberReqDto);

        given(authService.login(any())).willReturn(AuthFixture.postMemberReqDto);
        given(memberService.createMemberByOauthIdentifier(any())).willReturn(AuthFixture.authenticatedMember);
        given(authService.createToken(any())).willReturn(AuthFixture.createMemberTokensResDto);

        this.mockMvc.perform(
                post("/auth/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                        ))
                );
    }

    @DisplayName("로그아웃 요청 시, 로그아웃에 성공합니다.")
    @Test
    void logoutWithCookie() throws Exception {

        this.mockMvc.perform(
                delete("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .cookie(AuthFixture.getAccessTokenCookie())
                        .cookie(AuthFixture.getRefreshTokenCookie())
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());

    }

    @DisplayName("로그아웃 요청 시 쿠키가 없다면 바로 로그아웃에 성공합니다.")
    @Test
    void logoutWithNotCookie() throws Exception {

        this.mockMvc.perform(
                delete("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @DisplayName("토큰 재발급 요청에 성공합니다.")
    @Test
    void reissueByRefreshToken() throws Exception {

        given(authService.getMemberTokenByRefreshToken(any()))
                .willReturn(AuthFixture.memberToken);

        given(authService.reissueAccessToken(any()))
                .willReturn(AuthFixture.createMemberAccessTokenResDto);

        given(cookieProvider
                .setCookieWithOptions("accessToken", AuthFixture.createMemberAccessTokenResDto.getAccessToken()))
                .willReturn(AuthFixture.getAccessTokenCookie());

        this.mockMvc.perform(get("/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .cookie(AuthFixture.getRefreshTokenCookie())
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰")
                                )
                        )
                );
    }


    static Stream<String> provideOAuthNames() {
        return Stream.of("google", "github", "kakao");
    }
}