package com.ssafy.ssafsound.domain.auth.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberAccessTokenResDto;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberReqDto;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberTokensResDto;
import com.ssafy.ssafsound.domain.auth.service.AuthService;
import com.ssafy.ssafsound.domain.auth.service.CookieProvider;
import com.ssafy.ssafsound.domain.auth.util.ClientUtils;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberToken;
import com.ssafy.ssafsound.domain.member.dto.PostMemberReqDto;
import com.ssafy.ssafsound.domain.member.service.MemberService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public EnvelopeResponse<Void> logout(
            @CookieValue(value = "accessToken", defaultValue = "") String accessToken,
            @CookieValue(value = "refreshToken", defaultValue = "") String refreshToken,
            HttpServletResponse response) {
        authService.deleteTokens(accessToken, refreshToken);
        cookieProvider.setResponseWithCookies(response, null, null);
        return EnvelopeResponse.<Void>builder()
                .build();
    }

    @PostMapping("/reissue")
    public EnvelopeResponse<CreateMemberAccessTokenResDto> reissue(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response) {
        MemberToken memberToken = authService.getMemberTokenByRefreshToken(refreshToken);
        CreateMemberAccessTokenResDto createMemberAccessTokenResDto = authService.reissueAccessToken(memberToken);
        ResponseCookie accessTokenCookie = cookieProvider
                .setAccessTokenCookie(createMemberAccessTokenResDto.getAccessToken());

        response.setHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

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

        Member member = memberService.saveTokenByMember(authenticatedMember.getMemberId(), createMemberTokensResDto);

        cookieProvider.setResponseWithCookies(
                response,
                createMemberTokensResDto.getAccessToken(),
                createMemberTokensResDto.getRefreshToken());

        authService.saveClientLoginLog(member,
                ClientUtils.getClientDevice(),
                ClientUtils.getRemoteAddress());

        return EnvelopeResponse.<CreateMemberTokensResDto>builder()
                .data(createMemberTokensResDto)
                .build();
    }
}
