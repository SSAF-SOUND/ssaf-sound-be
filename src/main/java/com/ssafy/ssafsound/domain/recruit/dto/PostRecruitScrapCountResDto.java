package com.ssafy.ssafsound.domain.recruit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRecruitScrapCountResDto {
    private Long scrapCount;
    private boolean scraped;
}
