package com.ssafy.ssafsound.domain.lunch.task.domain;

import com.ssafy.ssafsound.domain.lunch.exception.LunchErrorInfo;
import com.ssafy.ssafsound.domain.lunch.exception.LunchException;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Component
@ConfigurationProperties(prefix = "scrap.welstory")
@RequiredArgsConstructor
public class WelstoryInfoProvider implements ScrapInfoProvider{

    private final RestTemplate restTemplate;
    private Map<String, String> info;
    private Credentials credentials;
    private Map<String, String> restaurantCode;

    public HttpHeaders getSessionHeader(){
        MultiValueMap<String, String> parameters = makeLoginParameters();

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    this.info.get("url"),
                    new HttpEntity<>(parameters),
                    Map.class
            );

            return response.getHeaders();

        } catch (RestClientException e) {
            log.error(e.getMessage());
            throw new LunchException(LunchErrorInfo.SCRAPING_ERROR);
        }
    }

    @Override
    public Map<String, String> scrapLunchInfo(MetaData campus) {

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        HttpHeaders headers = getSessionHeader();

        parameters.set("menuDt", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        parameters.set("menuMealType", this.info.get("menuMealType"));
        parameters.set("restaurantCode", this.restaurantCode.get(campus.getName()));

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(parameters, headers);

        return restTemplate.getForEntity(
                this.info.get("url"),
                Map.class,
                httpEntity).getBody();
    }

    private MultiValueMap<String, String> makeLoginParameters(){
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.set("username", this.credentials.username);
        parameters.set("password", this.credentials.password);
        parameters.set("remember-me",this.credentials.rememberMe);

        return parameters;
    }

    private static class Credentials{
        String url;
        String username;
        String password;
        String rememberMe;
    }
}
