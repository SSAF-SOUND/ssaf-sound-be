package com.ssafy.ssafsound.domain.recruit.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.recruit.dto.*;
import com.ssafy.ssafsound.domain.recruit.service.RecruitService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/recruits")
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitService recruitService;

    @PostMapping
    public EnvelopeResponse<Void> saveRecruit(@Authentication AuthenticatedMember memberInfo, @Valid @RequestBody PostRecruitReqDto recruitReqDto) {
        recruitService.saveRecruit(memberInfo.getMemberId(), recruitReqDto);
        return EnvelopeResponse.<Void>builder().build();
    }

    @PostMapping("/{recruitId}/scrap")
    public EnvelopeResponse<Void> toggleRecruitScrap(@PathVariable Long recruitId, @Authentication AuthenticatedMember memberInfo) {
        recruitService.toggleRecruitScrap(recruitId, memberInfo.getMemberId());
        return EnvelopeResponse.<Void>builder().build();
    }

    @PostMapping("/{recruitId}/expired")
    public EnvelopeResponse<Void> expiredRecruit(@PathVariable Long recruitId, @Authentication AuthenticatedMember memberInfo) {
        recruitService.expiredRecruit(recruitId, memberInfo.getMemberId());
        return EnvelopeResponse.<Void>builder().build();
    }

    @GetMapping("/{recruitId}")
    public EnvelopeResponse<GetRecruitDetailResDto> getRecruitDetail(@PathVariable Long recruitId) {
        return EnvelopeResponse.<GetRecruitDetailResDto>builder()
                .data(recruitService.getRecruitDetail(recruitId))
                .build();
    }

    @PatchMapping("/{recruitId}")
    public EnvelopeResponse<Void> updateRecruit(@PathVariable Long recruitId, @Authentication AuthenticatedMember memberInfo, @RequestBody PatchRecruitReqDto patchRecruitReqDto) {
        recruitService.updateRecruit(recruitId, memberInfo.getMemberId(), patchRecruitReqDto);
        return EnvelopeResponse.<Void>builder().build();
    }

    @DeleteMapping("/{recruitId}")
    public EnvelopeResponse<Void> deleteRecruit(@PathVariable Long recruitId, @Authentication AuthenticatedMember memberInfo) {
        recruitService.deleteRecruit(recruitId, memberInfo.getMemberId());
        return EnvelopeResponse.<Void>builder().build();
    }

    @GetMapping
    public EnvelopeResponse<GetRecruitsResDto> getRecruits(GetRecruitsReqDto getRecruitsReqDto, Pageable pageable) {
        return EnvelopeResponse.<GetRecruitsResDto>builder()
                .data(recruitService.getRecruits(getRecruitsReqDto, pageable))
                .build();
    }
}
