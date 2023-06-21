package com.ssafy.ssafsound.domain.lunch.task.domain;

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
        this.scrapInfosProviders.get(providerType);
    }
}
