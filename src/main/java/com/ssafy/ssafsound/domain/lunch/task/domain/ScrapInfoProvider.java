package com.ssafy.ssafsound.domain.lunch.task.domain;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;

import java.util.Map;

public interface ScrapInfoProvider {
    Map scrapLunchInfo(MetaData campus);
}
