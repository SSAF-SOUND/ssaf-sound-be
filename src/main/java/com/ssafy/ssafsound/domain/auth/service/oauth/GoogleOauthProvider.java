package com.ssafy.ssafsound.domain.auth.service.oauth;

import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
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
        Map<String, Object> params = new HashMap<>();

        params.put("scope", GOOGLE_DATA_ACCESS_SCOPE);
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_CLIENT_URL);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return GOOGLE_URL + "?" + parameterString;
    }

    @Override
    public String  getOauthAccessToken(String code) {
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();

        code = URLDecoder.decode(code);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        parameters.set("code", code);
        parameters.set("client_id", GOOGLE_CLIENT_ID);
        parameters.set("client_secret", GOOGLE_CLIENT_SECRET);
        parameters.set("redirect_uri", GOOGLE_CLIENT_URL);
        parameters.set("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, Object>> restRequest = new HttpEntity<>(parameters, headers);
        log.info("code: " + code);
        log.info("restRequest: " + restRequest);
        try {
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(GOOGLE_TOKEN_URL, restRequest, String.class);
            log.info("apiResponse: " + apiResponse);
            log.info("api body: " + apiResponse.getBody());
            return apiResponse.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AuthException(GlobalErrorInfo.AUTH_SERVER_ERROR);
        }
    }

    @Override
    public String getUserOauthIdentifier(String accessToken) {
        return null;
    }
}
