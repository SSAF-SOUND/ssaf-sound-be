package com.ssafy.ssafsound.domain.lunch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.ssafsound.domain.lunch.domain.FreshmealProperties;
import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.dto.GetFreshmealResDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetScrapReqDto;
import com.ssafy.ssafsound.global.common.json.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FreshmealInfoProvider implements ScrapInfoProvider {

    private final RestTemplate restTemplate;

    private final FreshmealProperties freshmealProperties;

    @Override
    public List<Lunch> scrapLunchInfo(List<GetScrapReqDto> getScrapReqDtos) {

        List<Lunch> lunches = new ArrayList<>();

        for (GetScrapReqDto getScrapReqDto : getScrapReqDtos) {
            Map<String, String> parameters = makeScrapParameters();

            String url = makeScrapUri(this.freshmealProperties.getUrl(), parameters);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    makeHeader(),
                    String.class
            );

            try {
                lunches.addAll(JsonParser.getMapper()
                        .readValue(response.getBody(), GetFreshmealResDto.class)
                        .getLunches(getScrapReqDto.getCampus()));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                e.printStackTrace();

                throw new RuntimeException();
            }
        }

        return lunches;
    }

    private HttpEntity makeHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        header.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity(header);
    }

    private Map<String, String> makeScrapParameters() {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("storeIdx", this.freshmealProperties.getStoreIdx());
        parameters.put("weekType", this.freshmealProperties.getWeekType());

        return parameters;
    }

    private String makeScrapUri(String baseUri, Map<String, String> parameters) {

        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(baseUri);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            uri.queryParam(entry.getKey(), entry.getValue());
        }

        return uri.toUriString();
    }
}
