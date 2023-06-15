package com.ssafy.ssafsound.domain.meta.domain;

import lombok.Getter;

@Getter
public class MetaData {

    private final int id;
    private final String name;

    public MetaData(MetaDataProvider metaDataProvider) {
        this.id = metaDataProvider.getId();
        this.name = metaDataProvider.getName();
    }
}
