package com.ssafy.ssafsound.domain.meta.controller;

import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.dto.GetCampusesResDto;
import com.ssafy.ssafsound.domain.meta.dto.GetRecruitTypeResDto;
import com.ssafy.ssafsound.domain.meta.dto.GetSkillsResDto;
import com.ssafy.ssafsound.domain.meta.service.EnumMetaDataConsumer;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
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
                .data(new GetCampusesResDto(consumer.getMetaDataList(MetaDataType.CAMPUS.name())))
                .build();
    }

    @GetMapping("/skills")
    public EnvelopeResponse<GetSkillsResDto> getSkills() {
        return EnvelopeResponse.<GetSkillsResDto>builder()
                .data(new GetSkillsResDto(consumer.getMetaDataList(MetaDataType.SKILL.name())))
                .build();
    }

    @GetMapping("/recruit-types")
    public EnvelopeResponse<GetRecruitTypeResDto> getRecruitTypes() {
        return EnvelopeResponse.<GetRecruitTypeResDto>builder()
                .data(new GetRecruitTypeResDto(consumer.getMetaDataList(MetaDataType.RECRUIT_TYPE.name())))
                .build();
    }

}
