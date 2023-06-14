package com.ssafy.ssafsound.domain.lunch.controller;

import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListReqDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchResDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListResDto;
import com.ssafy.ssafsound.domain.lunch.service.LunchService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("/lunch")
@RequiredArgsConstructor
public class LunchController {

    private final LunchService lunchService;

    @GetMapping("/{lunchId}")
    public EnvelopeResponse<GetLunchResDto> getLunchByLunchId(@PathVariable @NumberFormat Long lunchId){

        return EnvelopeResponse.<GetLunchResDto>builder()
                .code(HttpStatus.OK.toString())
                .message("success")
                .data(lunchService.findLunchByLunchId(lunchId))
                .build();
    }

    @GetMapping
    public EnvelopeResponse<GetLunchListResDto> getLunchesByCampusAndDate(@Valid GetLunchListReqDto getLunchListReqDto){

        return EnvelopeResponse.<GetLunchListResDto>builder()
                .code(HttpStatus.OK.toString())
                .message("success")
                .data(lunchService.findLunchesByCampusAndDate(getLunchListReqDto))
                .build();
    }
}
