package com.ssafy.ssafsound.domain.auth.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberReqDto;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberTokensResDto;
import com.ssafy.ssafsound.domain.auth.service.AuthService;
import com.ssafy.ssafsound.domain.auth.validator.AuthorizationExtractor;
import com.ssafy.ssafsound.domain.member.dto.PostMemberReqDto;
import com.ssafy.ssafsound.domain.member.service.MemberService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    public AuthController(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @GetMapping("/{oauthName}")
    public void socialLoginRedirect(@PathVariable(name = "oauthName") String oauthName,
                                    HttpServletResponse response) {
        authService.sendRedirectURL(oauthName, response);
    }

    @GetMapping("/logout")
    public EnvelopeResponse logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (Objects.nonNull(cookies)) {
            authService.deleteTokensByCookie(cookies);
            Cookie accessTokenCookie = deleteCookie("accessToken", null);
            Cookie refreshTokenCookie = deleteCookie("refreshToken", null);
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
        }
        return EnvelopeResponse.builder().build();
    }

    @GetMapping("/reissue")
    public EnvelopeResponse<CreateMemberTokensResDto> reissue(HttpServletRequest request) {
        String refreshToken = AuthorizationExtractor.extractToken("refreshToken", request);
        authService.validateRefreshToken(refreshToken);
        return EnvelopeResponse.<CreateMemberTokensResDto>builder()
                .build();
    }

    @PostMapping("/callback")
    public EnvelopeResponse<CreateMemberTokensResDto> login(
            @Valid @RequestBody CreateMemberReqDto createMemberReqDto,
            HttpServletResponse response) {
        PostMemberReqDto postMemberReqDto = authService.login(createMemberReqDto);
        AuthenticatedMember authenticatedMember = memberService.createMemberByOauthIdentifier(postMemberReqDto);
        CreateMemberTokensResDto createMemberTokensResDto = authService.createToken(authenticatedMember);
        memberService.saveTokenByMember(authenticatedMember, createMemberTokensResDto.getAccessToken(), createMemberTokensResDto.getRefreshToken());
        Cookie accessTokenCookie = setCookieWithOptions("accessToken", createMemberTokensResDto.getAccessToken());
        Cookie refreshTokenCookie = setCookieWithOptions("refreshToken", createMemberTokensResDto.getRefreshToken());
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        return EnvelopeResponse.<CreateMemberTokensResDto>builder()
                .data(createMemberTokensResDto)
                .build();
    }

    public Cookie setCookieWithOptions(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(2629744);
        cookie.setSecure(true);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie deleteCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setPath("/");
        return cookie;
    }
}
