package com.ssafy.ssafsound.domain.post.dto;

import lombok.Getter;

@Getter
public class PostPostScrapResDto {
    private final Integer scrapCount;
    private final Boolean scraped;

    public PostPostScrapResDto(Integer scrapCount, Boolean scraped) {
        this.scrapCount = scrapCount;
        this.scraped = scraped;
    }
}
