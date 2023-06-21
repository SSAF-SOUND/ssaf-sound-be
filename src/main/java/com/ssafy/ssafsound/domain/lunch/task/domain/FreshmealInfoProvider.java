package com.ssafy.ssafsound.domain.lunch.task.domain;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "scrap.freshmeal")
@RequiredArgsConstructor
@Setter
public class FreshmealInfoProvider implements ScrapInfoProvider{

    private final RestTemplate restTemplate;

    private String url;
    private String storeIdx;
    private String weekType;

    private List<String> attributes = new ArrayList<>();

    @Override
    public Map scrapLunchInfo(MetaData campus) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.set("storeIdx",this.storeIdx);
        parameters.set("weekType", this.weekType);

        return restTemplate.getForEntity(
                url,
                Map.class,
                parameters).getBody();
    }
}
