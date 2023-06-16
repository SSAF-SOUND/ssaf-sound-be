package com.ssafy.ssafsound.domain.auth.service.oauth;

import com.ssafy.ssafsound.domain.auth.domain.OauthType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OauthProviderFactory {
    private final GoogleOauthProvider googleOauthProvider;

    public OauthProviderFactory(GoogleOauthProvider googleOauthProvider) {
        this.googleOauthProvider = googleOauthProvider;
    }

    public OauthProvider of(String oauthName) {
        log.info("oauthName: " + oauthName);
        if (OauthType.GOOGLE.isEqual(oauthName)) {
            log.info("GoogleOauthProvider가 제공됩니다.");
            return googleOauthProvider;
        }
        return null;
    }
}
