package com.ssafy.ssafsound.domain.meta.controller;

import com.ssafy.ssafsound.domain.meta.dto.GetCampusesResDto;
import com.ssafy.ssafsound.domain.meta.dto.GetSkillsResDto;
import com.ssafy.ssafsound.domain.meta.service.EnumMetaDataConsumer;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meta")
@RequiredArgsConstructor
public class MetaDataController {

    private final EnumMetaDataConsumer consumer;

    @GetMapping("/campuses")
    public EnvelopeResponse<GetCampusesResDto> getCampuses() {
        return EnvelopeResponse.<GetCampusesResDto>builder()
                .code(HttpStatus.OK.toString())
                .message("success")
                .data(new GetCampusesResDto(consumer.getMetaDataList("skill")))
                .build();
    }

    @GetMapping("/skills")
    public EnvelopeResponse<GetSkillsResDto> getCSkills() {
        return EnvelopeResponse.<GetSkillsResDto>builder()
                .code(HttpStatus.OK.toString())
                .message("success")
                .data(new GetSkillsResDto(consumer.getMetaDataList("campus")))
                .build();
    }
}
