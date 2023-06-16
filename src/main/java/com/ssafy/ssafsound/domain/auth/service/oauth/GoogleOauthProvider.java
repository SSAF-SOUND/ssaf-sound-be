package com.ssafy.ssafsound.domain.auth.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class GoogleOauthProvider implements OauthProvider {

    private final RestTemplate restTemplate;
    @Value("${oauth2.google.url}")
    private String GOOGLE_URL;
    @Value("${oauth2.google.token-url}")
    private String GOOGLE_TOKEN_URL;
    @Value("${oauth2.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${oauth2.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${oauth2.google.client-url}")
    private String GOOGLE_CLIENT_URL;
    @Value("${oauth2.google.scope}")
    private String GOOGLE_DATA_ACCESS_SCOPE;

    @Override
    public String getOauthUrl() {
        return null;
    }

    @Override
    public String  getOauthAccessToken(String code) {
        return null;
    }

    @Override
    public String getUserOauthIdentifier(String accessToken) {
        return null;
    }
}
