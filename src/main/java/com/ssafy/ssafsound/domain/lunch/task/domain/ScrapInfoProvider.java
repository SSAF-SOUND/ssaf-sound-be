package com.ssafy.ssafsound.domain.lunch.task.domain;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import org.json.JSONObject;

public interface ScrapInfoProvider {
    JSONObject scrapLunchInfo(MetaData campus);
}
