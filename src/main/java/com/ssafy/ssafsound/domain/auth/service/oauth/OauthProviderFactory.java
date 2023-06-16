package com.ssafy.ssafsound.domain.auth.service.oauth;

import com.ssafy.ssafsound.domain.auth.domain.OauthType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OauthProviderFactory {
    private final GoogleOauthProvider googleOauthProvider;
    private final GithubOauthProvider githubOauthProvider;
    private final KakaoOauthProvider kakaoOauthProvider;

    public OauthProviderFactory(GoogleOauthProvider googleOauthProvider,
                                GithubOauthProvider githubOauthProvider,
                                KakaoOauthProvider kakaoOauthProvider) {
        this.googleOauthProvider = googleOauthProvider;
        this.githubOauthProvider = githubOauthProvider;
        this.kakaoOauthProvider = kakaoOauthProvider;
    }

    public OauthProvider of(String oauthName) {
        log.info("oauthName: " + oauthName);
        if (OauthType.GOOGLE.isEqual(oauthName)) {
            log.info("GoogleOauthProvider가 제공됩니다.");
            return googleOauthProvider;
        } else if (OauthType.GITHUB.isEqual(oauthName)) {
            log.info("GithubOauthProvider가 제공됩니다.");
            return githubOauthProvider;
        } else if (OauthType.KAKAO.isEqual(oauthName)) {
            log.info("KakaoOauthProvider가 제공됩니다.");
            return kakaoOauthProvider;
        }
        return null;
    }
}
