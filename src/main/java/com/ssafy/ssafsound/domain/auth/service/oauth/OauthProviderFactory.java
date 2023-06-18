package com.ssafy.ssafsound.domain.auth.service.oauth;

import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.domain.OAuthType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class OauthProviderFactory {

    Map<OAuthType, OauthProvider> oauthProviderMap;

    public OauthProviderFactory(GoogleOauthProvider googleOauthProvider,
                                GithubOauthProvider githubOauthProvider,
                                KakaoOauthProvider kakaoOauthProvider) {
        this.oauthProviderMap = new HashMap<>();
        oauthProviderMap.put(OAuthType.GOOGLE, googleOauthProvider);
        oauthProviderMap.put(OAuthType.KAKAO, kakaoOauthProvider);
        oauthProviderMap.put(OAuthType.GITHUB, githubOauthProvider);
    }

    public OauthProvider from(String oAuthType) {
        OAuthType enumType = Arrays.stream(OAuthType.values()).filter(type -> type.isEqual(oAuthType)).findFirst().orElse(null);
        if(enumType == null) throw new AuthException(MemberErrorInfo.AUTH_VALUE_NOT_FOUND);
        return this.oauthProviderMap.get(enumType);
    }
}
