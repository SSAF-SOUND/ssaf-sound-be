package com.ssafy.ssafsound.domain.lunch.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class GetLunchListReqDto {

    @NotBlank
    private String campus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
