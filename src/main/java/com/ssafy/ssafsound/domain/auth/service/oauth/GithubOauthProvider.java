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
public class GithubOauthProvider implements OauthProvider {

    private final RestTemplate restTemplate;
    @Value("${oauth2.github.url}")
    private String GITHUB_URL;
    @Value("${oauth2.github.token-url}")
    private String GITHUB_TOKEN_URL;
    @Value("${oauth2.github.client-id}")
    private String GITHUB_CLIENT_ID;
    @Value("${oauth2.github.client-secret}")
    private String GITHUB_CLIENT_SECRET;
    @Value("${oauth2.github.redirect-uri}")
    private String GITHUB_REDIRECT_URI;
    @Value("${oauth2.github.scope}")
    private String SCOPE;
    @Value("${oauth2.github.client-key-url}")
    private String GITHUB_USER_KEY;
    @Value("${oauth2.github.secret-key}")
    private String GITHUB_SECRET_KEY;

    @Override
    public String getOauthUrl() {
        return new StringBuilder().append(GITHUB_URL).append("?")
                .append("scope=").append(SCOPE).append("&")
                .append("client_id=").append(GITHUB_CLIENT_ID).append("&")
                .append("redirect_uri=").append(GITHUB_REDIRECT_URI).toString();
    }

    @Override
    public String getOauthAccessToken(String code) {
        HttpEntity<MultiValueMap<String, Object>> restRequest = settingParameters(code);

        try {
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(GITHUB_TOKEN_URL, restRequest, String.class);
            if (apiResponse.getBody() == null) throw new AuthException();
            return parsingAccessToken(apiResponse.getBody());
        } catch (RestClientException | AuthException e) {
            throw new AuthException(MemberErrorInfo.AUTH_SERVER_ERROR);
        }
    }

    @Override
    public PostMemberReqDto getMemberOauthIdentifier(String accessToken, String oauthName) {
        HttpEntity<MultiValueMap<String, String>> request = settingHeadersWithAccessToken(accessToken);

        try {
            ResponseEntity<String> apiResponse = restTemplate.exchange(
                    GITHUB_USER_KEY,
                    HttpMethod.GET,
                    request,
                    String.class);
            return PostMemberReqDto.builder()
                    .oauthIdentifier(parsingValue(apiResponse.getBody(), GITHUB_SECRET_KEY))
                    .oauthName(oauthName)
                    .build();
        } catch (RestClientException e) {
            throw new AuthException(MemberErrorInfo.AUTH_SERVER_ERROR);
        } catch (JsonProcessingException e) {
            throw new AuthException(MemberErrorInfo.AUTH_SERVER_PARSING_ERROR);
        }
    }

    public HttpEntity<MultiValueMap<String, Object>> settingParameters(String code) {
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();

        parameters.set("code", code);
        parameters.set("client_id", GITHUB_CLIENT_ID);
        parameters.set("client_secret", GITHUB_CLIENT_SECRET);
        parameters.set("redirect_uri", GITHUB_REDIRECT_URI);
        return new HttpEntity<>(parameters);
    }

    public HttpEntity<MultiValueMap<String, String>> settingHeadersWithAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+accessToken);
        return new HttpEntity<>(headers);
    }

    public String parsingAccessToken(String response) {
        for (String param : response.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals("access_token")) {
                return pair[1];
            }
        }
        throw new AuthException();
    }

    public String parsingValue(String response, String key) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(response, new TypeReference<>() {});
        return (String) jsonMap.get(key);
    }
}
