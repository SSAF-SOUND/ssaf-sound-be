package com.ssafy.ssafsound.domain.auth.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppleOauthProvider implements OauthProvider {
    @Override
    public String getOauthUrl() {
        return null;
    }

    @Override
    public String getOauthAccessToken(String code) {
        return null;
    }

    @Override
    public String getUserOauthIdentifier(String accessToken) {
        return null;
    }
}
