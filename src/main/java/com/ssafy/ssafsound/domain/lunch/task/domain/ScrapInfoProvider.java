package com.ssafy.ssafsound.domain.lunch.task.domain;

import com.ssafy.ssafsound.domain.lunch.task.dto.GetScrapReqDto;
import com.ssafy.ssafsound.domain.lunch.task.dto.GetScrapResDto;

public interface ScrapInfoProvider {
    GetScrapResDto scrapLunchInfo(GetScrapReqDto getScrapReqDto);
}
