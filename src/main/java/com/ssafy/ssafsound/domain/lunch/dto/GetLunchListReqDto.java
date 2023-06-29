package com.ssafy.ssafsound.domain.lunch.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class GetLunchListReqDto {

    @NotBlank
    private String campus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
