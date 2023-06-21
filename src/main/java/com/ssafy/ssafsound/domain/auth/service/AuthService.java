package com.ssafy.ssafsound.domain.auth.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.dto.CreateAccessTokenResDto;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberReqDto;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberTokensResDto;
import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.exception.AuthErrorInfo;
import com.ssafy.ssafsound.domain.auth.service.oauth.OauthProvider;
import com.ssafy.ssafsound.domain.auth.service.oauth.OauthProviderFactory;
import com.ssafy.ssafsound.domain.auth.service.token.JwtTokenProvider;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberToken;
import com.ssafy.ssafsound.domain.member.dto.PostMemberReqDto;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class AuthService {

    private final OauthProviderFactory oauthProviderFactory;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberTokenRepository memberTokenRepository;

    public AuthService(OauthProviderFactory oauthProviderFactory, JwtTokenProvider jwtTokenProvider, MemberTokenRepository memberTokenRepository) {
        this.oauthProviderFactory = oauthProviderFactory;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberTokenRepository = memberTokenRepository;
    }

    public void sendRedirectURL(String oauthName, HttpServletResponse response) {
        OauthProvider oauthProvider = oauthProviderFactory.from(oauthName);
        try {
            String redirectURL = oauthProvider.getOauthUrl();
            response.sendRedirect(redirectURL);
        } catch (Exception e) {
            throw new AuthException(AuthErrorInfo.AUTH_SERVER_ERROR);
        }
    }

    public PostMemberReqDto login(CreateMemberReqDto createMemberReqDto) {
        OauthProvider oauthProvider = oauthProviderFactory.from(createMemberReqDto.getOauthName());
        String accessToken = oauthProvider.getOauthAccessToken(createMemberReqDto.getCode());
        return oauthProvider.getMemberOauthIdentifier(accessToken, createMemberReqDto.getOauthName());
    }

    public CreateMemberTokensResDto createToken(AuthenticatedMember authenticatedMember) {
        return CreateMemberTokensResDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(authenticatedMember))
                .refreshToken(jwtTokenProvider.createRefreshToken(authenticatedMember))
                .build();
    }

    @Transactional
    public void deleteTokensByCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            String token = cookie.getValue();
            if (jwtTokenProvider.isValid(token) && cookie.getName().equals("accessToken")) {
                AuthenticatedMember authenticatedMember = jwtTokenProvider.getParsedClaims(token);
                memberTokenRepository.deleteById(authenticatedMember.getMemberId());
            }
        }
    }

    @Transactional(readOnly = true)
    public Long validateRefreshToken(String refreshToken) {
        Long memberId = jwtTokenProvider.getMemberIdByRefreshToken(refreshToken);
        MemberToken memberToken = memberTokenRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_TOKEN_NOT_FOUND));
        if(isNotEqualRefreshToken(refreshToken, memberToken.getRefreshToken())) throw new AuthException(AuthErrorInfo.AUTH_TOKEN_INVALID);
        return memberId;
    }

    @Transactional
    public CreateAccessTokenResDto reIssueAccessToken(Long memberId) {
        MemberToken memberToken = memberTokenRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_TOKEN_NOT_FOUND));
        Member member = memberToken.getMember();
        String accessToken = jwtTokenProvider.createAccessToken(AuthenticatedMember.of(member));
        memberToken.changeAccessTokenByRefreshToken(accessToken);
        memberTokenRepository.save(memberToken);
        return CreateAccessTokenResDto.of(accessToken);
    }

    public boolean isNotEqualRefreshToken(String refreshTokenByCookie, String refreshTokenBySaved) {
        return !refreshTokenByCookie.equals(refreshTokenBySaved);
    }
}
