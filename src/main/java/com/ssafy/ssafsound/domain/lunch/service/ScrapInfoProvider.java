package com.ssafy.ssafsound.domain.lunch.service;

import com.ssafy.ssafsound.domain.lunch.dto.GetScrapReqDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetScrapResDto;

public interface ScrapInfoProvider {
    GetScrapResDto scrapLunchInfo(GetScrapReqDto getScrapReqDto);
}
