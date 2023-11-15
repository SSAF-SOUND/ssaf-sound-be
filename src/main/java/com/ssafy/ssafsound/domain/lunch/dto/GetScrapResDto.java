package com.ssafy.ssafsound.domain.lunch.dto;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;

import java.util.List;

public interface GetScrapResDto {
    List<Lunch> getLunches(MetaData campus);
}
