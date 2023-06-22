package com.ssafy.ssafsound.domain.auth.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.exception.AuthErrorInfo;
import com.ssafy.ssafsound.domain.member.dto.PostMemberReqDto;
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
import java.util.Map;

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
    @Value("${oauth2.google.secret-key}")
    private String GOOGLE_SECRET_KEY;
    @Value("${oauth2.google.response-type}")
    private String GOOGLE_RESPONSE_TYPE;

    @Override
    public String getOauthUrl() {
        return new StringBuilder().append(GOOGLE_URL).append("?")
                .append("scope=").append(GOOGLE_DATA_ACCESS_SCOPE).append("&")
                .append("response_type=").append(GOOGLE_RESPONSE_TYPE).append("&")
                .append("client_id=").append(GOOGLE_CLIENT_ID).append("&")
                .append("redirect_uri=").append(GOOGLE_CLIENT_URL).toString();
    }

    @Override
    public String getOauthAccessToken(String code) {
        HttpEntity<MultiValueMap<String, Object>> restRequest = settingParameters(code);

        try {
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(GOOGLE_TOKEN_URL, restRequest, String.class);
            return parsingValue(apiResponse.getBody(), "access_token");
        } catch (RestClientException e) {
            throw new AuthException(AuthErrorInfo.AUTH_SERVER_ERROR);
        } catch (JsonProcessingException e) {
            throw new AuthException(AuthErrorInfo.AUTH_SERVER_PARSING_ERROR);
        }
    }

    @Override
    public PostMemberReqDto getMemberOauthIdentifier(String accessToken, String oauthName) {
        HttpEntity<MultiValueMap<String, String>> request = settingHeader(accessToken);
        try {
            ResponseEntity<String> apiResponse = restTemplate.exchange(
                    GOOGLE_USER_KEY,
                    HttpMethod.GET, request,
                    String.class);
            return PostMemberReqDto.builder()
                    .oauthIdentifier(parsingValue(apiResponse.getBody(), GOOGLE_SECRET_KEY))
                    .oauthName(oauthName)
                    .build();
        } catch (RestClientException e) {
            throw new AuthException(AuthErrorInfo.AUTH_SERVER_ERROR);
        } catch (JsonProcessingException e) {
            throw new AuthException(AuthErrorInfo.AUTH_SERVER_PARSING_ERROR);
        }
    }

    public String parsingValue(String response, String key) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(response, new TypeReference<>() {});
        return (String) jsonMap.get(key);
    }
    
    public HttpEntity<MultiValueMap<String, Object>> settingParameters(String code) {
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();

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
