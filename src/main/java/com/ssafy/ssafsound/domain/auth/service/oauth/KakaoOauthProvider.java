package com.ssafy.ssafsound.domain.auth.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.auth.exception.MemberErrorInfo;
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
public class KakaoOauthProvider implements OauthProvider {

    private final RestTemplate restTemplate;
    @Value("${oauth2.kakao.url}")
    private String KAKAO_URL;
    @Value("${oauth2.kakao.token-url}")
    private String KAKAO_TOKEN_URL;
    @Value("${oauth2.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${oauth2.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;
    @Value("${oauth2.kakao.scope}")
    private String SCOPE;
    @Value("${oauth2.kakao.response_type}")
    private String RESPONSE_TYPE;
    @Value("${oauth2.kakao.grant_type}")
    private String GRANT_TYPE;
    @Value("${oauth2.kakao.client-key-url}")
    private String KAKAO_USER_KEY;
    @Value("${oauth2.kakao.secret-key}")
    private String KAKAO_SECRET_KEY;
    @Override
    public String getOauthUrl() {
        return new StringBuilder().append(KAKAO_URL).append("?")
                .append("scope=").append(SCOPE).append("&")
                .append("client_id=").append(KAKAO_CLIENT_ID).append("&")
                .append("redirect_uri=").append(KAKAO_REDIRECT_URI).append("&")
                .append("response_type=").append(RESPONSE_TYPE).toString();
    }

    @Override
    public String getOauthAccessToken(String code) {
        HttpEntity<MultiValueMap<String, Object>> restRequest = settingParameters(code);
        try {
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(KAKAO_TOKEN_URL, restRequest, String.class);
            return parsingValue(apiResponse.getBody(), "access_token");
        } catch (RestClientException | JsonProcessingException e) {
            throw new AuthException(MemberErrorInfo.AUTH_SERVER_ERROR);
        }
    }

    @Override
    public PostMemberReqDto getUserOauthIdentifier(String accessToken, String oauthName) {
        HttpEntity<MultiValueMap<String, String>> request = settingHeader(accessToken);
        try {
            ResponseEntity<String> apiResponse = restTemplate.exchange(
                    KAKAO_USER_KEY,
                    HttpMethod.GET, request,
                    String.class);
            return PostMemberReqDto.builder()
                    .oauthIdentifier(parsingValue(apiResponse.getBody(), KAKAO_SECRET_KEY))
                    .oauthName(oauthName)
                    .build();
        } catch (RestClientException | JsonProcessingException e) {
            throw new AuthException(MemberErrorInfo.AUTH_SERVER_ERROR);
        }
    }

    public String parsingValue(String response, String key) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(response, new TypeReference<>() {});
        return String.valueOf(jsonMap.get(key));
    }

    public HttpEntity<MultiValueMap<String, Object>> settingParameters(String code) {
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();

        parameters.set("code", code);
        parameters.set("client_id", KAKAO_CLIENT_ID);
        parameters.set("redirect_uri", KAKAO_REDIRECT_URI);
        parameters.set("grant_type", GRANT_TYPE);
        return new HttpEntity<>(parameters);
    }

    public HttpEntity<MultiValueMap<String, String>> settingHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+accessToken);
        return new HttpEntity<>(headers);
    }
}
