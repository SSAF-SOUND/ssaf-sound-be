package com.ssafy.ssafsound.domain.auth.controller;

import com.ssafy.ssafsound.domain.auth.service.AuthService;
import com.ssafy.ssafsound.domain.auth.service.CookieProvider;
import com.ssafy.ssafsound.domain.member.service.MemberService;
import com.ssafy.ssafsound.global.docs.RestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends RestDocsTest {

    @MockBean
    MemberService memberService;
    @MockBean
    CookieProvider cookieProvider;

    @MockBean
    AuthService authService;

    @DisplayName("oauth 로그인 요청 시, Redirect 를 제공한다.")
    @ParameterizedTest
    @MethodSource("provideOAuthNames")
    public void socialLoginRedirect(String oauthName) throws Exception {

        ResultActions resultActions =
                this.mockMvc.perform(
                get("/auth/{oauthName}", oauthName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"));

        resultActions
                .andExpect(status().isOk())
                .andDo(restDocs.document(pathParameters(
                        parameterWithName("oauthName").description("google, kakao, github, apple 중 하나를 입력하세요")
                )));


    }
    static Stream<String> provideOAuthNames() {
        return Stream.of("google", "github", "kakao");
    }
}