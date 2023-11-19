package com.ssafy.ssafsound.domain.lunch.service;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.dto.GetScrapReqDto;

import java.util.List;

public interface ScrapInfoProvider {
    List<Lunch> scrapLunchInfo(List<GetScrapReqDto> getScrapReqDtos);
}
