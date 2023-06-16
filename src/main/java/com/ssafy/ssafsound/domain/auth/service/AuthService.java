package com.ssafy.ssafsound.domain.auth.service;

import com.ssafy.ssafsound.domain.auth.dto.CreateMemberReqDto;
import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.service.oauth.OauthProvider;
import com.ssafy.ssafsound.domain.auth.service.oauth.OauthProviderFactory;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final OauthProviderFactory oauthProviderFactory;

    public AuthService(OauthProviderFactory oauthProviderFactory) {
        this.oauthProviderFactory = oauthProviderFactory;
    }

    public String login(CreateMemberReqDto createMemberReqDto) {
        OauthProvider oauthProvider = oauthProviderFactory.of(createMemberReqDto.getOauthName());
        if(oauthProvider == null) {
            log.error("oauthProvider가 null입니다.");
            throw new AuthException(GlobalErrorInfo.AUTH_VALUE_NOT_FOUND);
        }
        log.info("oauthProvider: " + oauthProvider.getClass());
        String accessToken = oauthProvider.getOauthAccessToken(createMemberReqDto.getCode());
        return oauthProvider.getUserOauthIdentifier(accessToken);
    }
}
