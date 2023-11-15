package com.ssafy.ssafsound.domain.lunch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.domain.WelstoryProperties;
import com.ssafy.ssafsound.domain.lunch.dto.GetScrapReqDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetWelstoryResDto;
import com.ssafy.ssafsound.domain.lunch.exception.LunchErrorInfo;
import com.ssafy.ssafsound.domain.lunch.exception.LunchException;
import com.ssafy.ssafsound.global.common.json.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WelstoryInfoProvider implements ScrapInfoProvider {

    private final RestTemplate restTemplate;
    private final WelstoryProperties welstoryProperties;

    private HttpEntity header;

    @Override
    public List<Lunch> scrapLunchInfo(List<GetScrapReqDto> getScrapReqDtos) {

        this.setSessionHeader();

        List<Lunch> lunches = new ArrayList<>();

        for (GetScrapReqDto getScrapReqDto : getScrapReqDtos) {
            Map<String, String> parameters = makeScrapParameters(getScrapReqDto);
            String url = makeUri(this.welstoryProperties.getInfo().get("url"), parameters);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    header,
                    String.class);

            try {
                lunches.addAll(JsonParser.getMapper()
                        .readValue(response.getBody(), GetWelstoryResDto.class)
                        .getLunches(getScrapReqDto.getCampus()));
            } catch (JsonProcessingException e) {
                throw new LunchException(LunchErrorInfo.SCRAPING_ERROR);
            }
        }

        return lunches;

    }

    private void setSessionHeader() {
        HttpHeaders header = new HttpHeaders();

        header.add("Cookie", getJSESSIONID());
        header.add("Content-Type", "application/json");
        header.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        this.header = new HttpEntity(header);
    }

    private String getJSESSIONID() {
        Map<String, String> parameters = makeLoginParameters();

        String url = makeUri(this.welstoryProperties.getCredentials().getUrl(), parameters);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                null,
                String.class
        );

        return response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }

    private Map<String, String> makeLoginParameters() {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("username", this.welstoryProperties.getCredentials().getUsername());
        parameters.put("password", this.welstoryProperties.getCredentials().getPassword());
        parameters.put("remember-me", this.welstoryProperties.getCredentials().getRememberMe());

        return parameters;
    }

    private Map<String, String> makeScrapParameters(GetScrapReqDto getScrapReqDto) {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("menuDt", getScrapReqDto.getMenuDt().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        parameters.put("menuMealType", this.welstoryProperties.getInfo().get("menuMealType"));
        parameters.put("restaurantCode", this.welstoryProperties.getRestaurantCode().get(getScrapReqDto.getCampus().getName()));

        return parameters;
    }

    private String makeUri(String baseUri, Map<String, String> parameters) {

        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(baseUri);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            uri.queryParam(entry.getKey(), entry.getValue());
        }

        return uri.toUriString();
    }
}
