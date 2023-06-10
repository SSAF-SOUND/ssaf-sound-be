package com.ssafy.ssafsound.domain.lunch.controller;

import com.ssafy.ssafsound.domain.lunch.dto.MenuDetail;
import com.ssafy.ssafsound.domain.lunch.dto.Menus;
import com.ssafy.ssafsound.domain.lunch.service.LunchService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/lunch")
@RequiredArgsConstructor
public class LunchController {

    private final LunchService lunchService;

    @GetMapping("/{lunchId}")
    public EnvelopeResponse<MenuDetail> getLunchByLunchId(@PathVariable String lunchId){

        return EnvelopeResponse.<MenuDetail>builder()
                .code(HttpStatus.OK.toString())
                .message("success")
                .data(lunchService.findLunchByLunchId())
                .build();
    }

    @GetMapping
    public EnvelopeResponse<Menus> getLunchesByCampusAndDate(Integer campus, String date){

        return EnvelopeResponse.<Menus>builder()
                .code(HttpStatus.OK.toString())
                .message("success")
                .data(lunchService.findLunchesByCampusAndDate())
                .build();
    }
}
