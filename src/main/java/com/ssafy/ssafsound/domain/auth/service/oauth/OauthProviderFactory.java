package com.ssafy.ssafsound.domain.auth.service.oauth;

import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.domain.OauthType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class OauthProviderFactory {

    Map<OauthType, OauthProvider> oauthProviderMap;

    public OauthProviderFactory(GoogleOauthProvider googleOauthProvider,
                                GithubOauthProvider githubOauthProvider,
                                KakaoOauthProvider kakaoOauthProvider) {
        this.oauthProviderMap = new HashMap<>();
        oauthProviderMap.put(OauthType.GOOGLE, googleOauthProvider);
        oauthProviderMap.put(OauthType.KAKAO, kakaoOauthProvider);
        oauthProviderMap.put(OauthType.GITHUB, githubOauthProvider);
    }

    public OauthProvider from(String oauthType) {
        OauthType enumType = Arrays.stream(OauthType.values()).filter(type -> type.isEqual(oauthType)).findFirst().orElse(null);
        if(enumType == null) throw new AuthException(MemberErrorInfo.AUTH_VALUE_NOT_FOUND);
        return this.oauthProviderMap.get(enumType);
    }
}
