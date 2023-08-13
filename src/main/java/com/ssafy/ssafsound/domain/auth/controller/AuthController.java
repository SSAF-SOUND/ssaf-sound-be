package com.ssafy.ssafsound.domain.auth.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberAccessTokenResDto;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberReqDto;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberTokensResDto;
import com.ssafy.ssafsound.domain.auth.service.AuthService;
import com.ssafy.ssafsound.domain.auth.service.CookieProvider;
import com.ssafy.ssafsound.domain.member.domain.MemberToken;
import com.ssafy.ssafsound.domain.member.dto.PostMemberReqDto;
import com.ssafy.ssafsound.domain.member.service.MemberService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final CookieProvider cookieProvider;

    public AuthController(AuthService authService, MemberService memberService, CookieProvider cookieProvider) {
        this.authService = authService;
        this.memberService = memberService;
        this.cookieProvider = cookieProvider;
    }

    @GetMapping("/{oauthName}")
    public void socialLoginRedirect(@PathVariable(name = "oauthName") String oauthName,
                                    HttpServletResponse response) {
        authService.sendRedirectURL(oauthName, response);
    }

    @DeleteMapping("/logout")
    public EnvelopeResponse logout(@CookieValue(value = "accessToken", defaultValue = "") String accessToken,
                                   @CookieValue(value = "refreshToken", defaultValue = "") String refreshToken,
                                   HttpServletResponse response) {

        if (!accessToken.equals("") || !refreshToken.equals("")) {
            authService.deleteTokens(accessToken, refreshToken);
            cookieProvider.setResponseWithCookies(response, null, null);
        }
        return EnvelopeResponse.builder().build();
    }

    @GetMapping("/reissue")
    public EnvelopeResponse<CreateMemberAccessTokenResDto> reissue(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response) {
        MemberToken memberToken = authService.getMemberTokenByRefreshToken(refreshToken);
        CreateMemberAccessTokenResDto createMemberAccessTokenResDto = authService.reissueAccessToken(memberToken);
        Cookie accessTokenCookie = cookieProvider
                .setCookieWithOptions("accessToken", createMemberAccessTokenResDto.getAccessToken());

        response.addCookie(accessTokenCookie);

        return EnvelopeResponse.<CreateMemberAccessTokenResDto>builder()
                .data(createMemberAccessTokenResDto)
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
        cookieProvider.setResponseWithCookies(response, createMemberTokensResDto.getAccessToken(), createMemberTokensResDto.getRefreshToken());
        return EnvelopeResponse.<CreateMemberTokensResDto>builder()
                .data(createMemberTokensResDto)
                .build();
    }
}
