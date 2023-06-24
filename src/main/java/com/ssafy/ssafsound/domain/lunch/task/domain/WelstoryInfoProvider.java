package com.ssafy.ssafsound.domain.lunch.task.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.ssafsound.domain.lunch.task.dto.GetScrapReqDto;
import com.ssafy.ssafsound.domain.lunch.task.dto.GetScrapResDto;
import com.ssafy.ssafsound.domain.lunch.task.dto.GetWelstoryResDto;
import com.ssafy.ssafsound.global.common.json.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WelstoryInfoProvider implements ScrapInfoProvider{

    private final RestTemplate restTemplate;
    private final WelstoryProperties welstoryProperties;

    private HttpEntity header;

    @Override
    public GetScrapResDto scrapLunchInfo(GetScrapReqDto getScrapReqDto) {

        Map<String, String> parameters = makeScrapParameters(getScrapReqDto);
        String url = makeUri(this.welstoryProperties.getInfo().get("url"), parameters);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                header,
                String.class);

        try {
            return JsonParser.getMapper().readValue(response.getBody(), GetWelstoryResDto.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();

            throw new RuntimeException();
        }
    }

    public void setSessionHeader() {
        HttpHeaders header = new HttpHeaders();

        header.add("Cookie",getJSESSIONID());
        header.add("Content-Type","application/json");
        header.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        this.header = new HttpEntity(header);
    }

    private String getJSESSIONID(){
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

    private String makeUri(String baseUri, Map<String, String> parameters){

        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(baseUri);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            uri.queryParam(entry.getKey(), entry.getValue());
        }

        return uri.toUriString();
    }
}
