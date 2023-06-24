package com.ssafy.ssafsound.domain.lunch.task.dto;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class GetScrapReqDto {
    MetaData campus;

    LocalDate menuDt;
}
