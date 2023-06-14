package com.ssafy.ssafsound.domain.lunch.dto;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class GetLunchListReqDto {

    @NotNull
    private Integer campusId;

    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate date;
}
