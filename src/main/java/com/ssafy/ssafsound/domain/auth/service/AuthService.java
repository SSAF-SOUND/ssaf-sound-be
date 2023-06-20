package com.ssafy.ssafsound.domain.auth.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberReqDto;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberTokensResDto;
import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.auth.service.oauth.OauthProvider;
import com.ssafy.ssafsound.domain.auth.service.oauth.OauthProviderFactory;
import com.ssafy.ssafsound.domain.auth.service.token.JwtTokenProvider;
import com.ssafy.ssafsound.domain.member.dto.PostMemberReqDto;
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
            throw new AuthException(MemberErrorInfo.AUTH_SERVER_ERROR);
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
                .refreshToken(jwtTokenProvider.createRefreshToken())
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
    public void validateRefreshToken(String refreshToken) {

    }
}
