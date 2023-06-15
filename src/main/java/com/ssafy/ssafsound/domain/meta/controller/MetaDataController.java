package com.ssafy.ssafsound.domain.meta.controller;

import com.ssafy.ssafsound.domain.meta.dto.GetCampusesRepDto;
import com.ssafy.ssafsound.domain.meta.dto.GetSkillsRepDto;
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
    public EnvelopeResponse<GetCampusesRepDto> getCampuses() {
        return EnvelopeResponse.<GetCampusesRepDto>builder()
                .code(HttpStatus.OK.toString())
                .message("success")
                .data(new GetCampusesRepDto(consumer.getMetaDataList("skill")))
                .build();
    }

    @GetMapping("/skills")
    public EnvelopeResponse<GetSkillsRepDto> getCSkills() {
        return EnvelopeResponse.<GetSkillsRepDto>builder()
                .code(HttpStatus.OK.toString())
                .message("success")
                .data(new GetSkillsRepDto(consumer.getMetaDataList("campus")))
                .build();
    }
}
