package com.ssafy.ssafsound.domain.lunch.service;

import com.ssafy.ssafsound.domain.lunch.service.FreshmealInfoProvider;
import com.ssafy.ssafsound.domain.lunch.service.ScrapInfoProvider;
import com.ssafy.ssafsound.domain.lunch.service.WelstoryInfoProvider;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScrapInfoProviderFactory {

    Map<String, ScrapInfoProvider> scrapInfosProviders = new HashMap<>();

    public ScrapInfoProviderFactory(WelstoryInfoProvider welstoryInfoProvider, FreshmealInfoProvider freshmealInfoProvider) {
        this.scrapInfosProviders.put("welstory", welstoryInfoProvider);
        this.scrapInfosProviders.put("freshmeal", freshmealInfoProvider);
    }

    public ScrapInfoProvider getProviderFrom(String providerType) {
        return this.scrapInfosProviders.get(providerType);
    }
}
