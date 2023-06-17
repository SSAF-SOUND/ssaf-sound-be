package com.ssafy.ssafsound.domain.auth.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
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
    @Value("${oauth2.google.client-key-url}")
    private String GOOGLE_USER_KEY;

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
    public String getOauthAccessToken(String code) {
        HttpEntity<MultiValueMap<String, Object>> restRequest = settingParameters(code);
        log.info("code: " + code);
        log.info("restRequest: " + restRequest);
        try {
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(GOOGLE_TOKEN_URL, restRequest, String.class);
            log.info("apiResponse: " + apiResponse);
            log.info("api body: " + apiResponse.getBody());
            String accessToken = parsingValue(apiResponse.getBody(), "access_token");
            log.info("access_token: " + accessToken);
            return accessToken;
        } catch (RestClientException | JsonProcessingException e) {
            log.error(e.getMessage());
            throw new AuthException(GlobalErrorInfo.AUTH_SERVER_ERROR);
        }
    }

    @Override
    public String getUserOauthIdentifier(String accessToken) {
        HttpEntity<MultiValueMap<String, String>> request = settingHeader(accessToken);
        try {
            ResponseEntity<String> apiResponse = restTemplate.exchange(
                    GOOGLE_USER_KEY,
                    HttpMethod.GET, request,
                    String.class);
            log.info("success:" + apiResponse.getBody());
            String oauthIdentifier = parsingValue(apiResponse.getBody(), "email");
            log.info("oauthIdentifier: " + oauthIdentifier);
            return oauthIdentifier;
        } catch (RestClientException | JsonProcessingException e) {
            throw new AuthException(GlobalErrorInfo.AUTH_SERVER_ERROR);
        }
    }

    public String parsingValue(String response, String key) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(response, new TypeReference<>() {});
        return (String) jsonMap.get(key);
    }
    
    public HttpEntity<MultiValueMap<String, Object>> settingParameters(String code) {
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();

        code = URLDecoder.decode(code);
        parameters.set("code", code);
        parameters.set("client_id", GOOGLE_CLIENT_ID);
        parameters.set("client_secret", GOOGLE_CLIENT_SECRET);
        parameters.set("redirect_uri", GOOGLE_CLIENT_URL);
        parameters.set("grant_type", "authorization_code");
        return new HttpEntity<>(parameters);
    }

    public HttpEntity<MultiValueMap<String, String>> settingHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+accessToken);
        return new HttpEntity<>(headers);
    }
}
