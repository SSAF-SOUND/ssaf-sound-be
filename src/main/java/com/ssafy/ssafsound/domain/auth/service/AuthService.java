package com.ssafy.ssafsound.domain.auth.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberAccessTokenResDto;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberReqDto;
import com.ssafy.ssafsound.domain.auth.dto.CreateMemberTokensResDto;
import com.ssafy.ssafsound.domain.auth.exception.AuthErrorInfo;
import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.service.oauth.OauthProvider;
import com.ssafy.ssafsound.domain.auth.service.oauth.OauthProviderFactory;
import com.ssafy.ssafsound.domain.auth.service.token.JwtTokenProvider;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberLoginLog;
import com.ssafy.ssafsound.domain.member.domain.MemberToken;
import com.ssafy.ssafsound.domain.member.dto.PostMemberReqDto;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberLoginLogRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.time.Clock;
import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthService {

    private final OauthProviderFactory oauthProviderFactory;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberTokenRepository memberTokenRepository;
    private final MemberLoginLogRepository memberLoginLogRepository;
    private final Clock clock;

    public AuthService(
            OauthProviderFactory oauthProviderFactory,
            JwtTokenProvider jwtTokenProvider,
            MemberTokenRepository memberTokenRepository,
            MemberLoginLogRepository memberLoginLogRepository,
            Clock clock) {
        this.oauthProviderFactory = oauthProviderFactory;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberTokenRepository = memberTokenRepository;
        this.memberLoginLogRepository = memberLoginLogRepository;
        this.clock = clock;
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

    @Transactional(readOnly = true)
    public MemberToken getMemberTokenByRefreshToken(String refreshToken) {
        Long memberId = jwtTokenProvider.getMemberIdByRefreshToken(refreshToken);
        MemberToken memberToken = memberTokenRepository
                .findById(memberId).orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_TOKEN_NOT_FOUND));

        if(isNotEqualRefreshToken(refreshToken, memberToken.getRefreshToken())) {
            throw new AuthException(AuthErrorInfo.AUTH_TOKEN_INVALID);
        }
        return memberToken;
    }

    @Transactional
    public CreateMemberAccessTokenResDto reissueAccessToken(MemberToken memberToken) {
        Member member = memberToken.getMember();
        String accessToken = jwtTokenProvider.createAccessToken(AuthenticatedMember.from(member));

        memberToken.changeAccessTokenByRefreshToken(accessToken);

        return CreateMemberAccessTokenResDto.of(accessToken);
    }

    @Transactional
    public void saveClientLoginLog(Member member, String clientDevice, String remoteAddress) {
        MemberLoginLog memberLoginLog = MemberLoginLog
                .ofCreateLoginLog(member, clientDevice, remoteAddress, LocalDateTime.now(clock));
        memberLoginLogRepository.save(memberLoginLog);
    }

    public boolean isNotEqualRefreshToken(String refreshTokenByCookie, String refreshTokenBySaved) {
        return !refreshTokenByCookie.equals(refreshTokenBySaved);
    }
}
