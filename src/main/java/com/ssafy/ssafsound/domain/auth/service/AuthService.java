package com.ssafy.ssafsound.domain.auth.service;

import com.ssafy.ssafsound.domain.auth.dto.CreateMemberReqDto;
import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.auth.service.oauth.OauthProvider;
import com.ssafy.ssafsound.domain.auth.service.oauth.OauthProviderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class AuthService {

    private final OauthProviderFactory oauthProviderFactory;
    private OauthProvider oauthProvider;

    public AuthService(OauthProviderFactory oauthProviderFactory) {
        this.oauthProviderFactory = oauthProviderFactory;
    }

    public void sendRedirectURL(String oauthName, HttpServletResponse response) {
        oauthProvider = oauthProviderFactory.of(oauthName);
        try {
            String redirectURL = oauthProvider.getOauthUrl();
            response.sendRedirect(redirectURL);
        } catch (Exception e) {
            throw new AuthException(MemberErrorInfo.AUTH_SERVER_ERROR);
        }
    }

    public String login(CreateMemberReqDto createMemberReqDto) {
        oauthProvider = oauthProviderFactory.of(createMemberReqDto.getOauthName());
        if(oauthProvider == null) {
            throw new AuthException(MemberErrorInfo.AUTH_VALUE_NOT_FOUND);
        }
        log.info("oauthProvider: " + oauthProvider.getClass());
        String accessToken = oauthProvider.getOauthAccessToken(createMemberReqDto.getCode());;
        return oauthProvider.getUserOauthIdentifier(accessToken);
    }
}
