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
    public EnvelopeResponse<PostRecruitResDto> saveRecruit(@Authentication AuthenticatedMember memberInfo, @Valid @RequestBody PostRecruitReqDto recruitReqDto) {
        return EnvelopeResponse.<PostRecruitResDto>builder()
                .data(recruitService.saveRecruit(memberInfo.getMemberId(), recruitReqDto))
                .build();
    }

    @PostMapping("/{recruitId}/scrap")
    public EnvelopeResponse<PostRecruitScrapCountResDto> toggleRecruitScrap(@PathVariable Long recruitId, @Authentication AuthenticatedMember memberInfo) {
        return EnvelopeResponse.<PostRecruitScrapCountResDto>builder()
                .data(recruitService.toggleRecruitScrap(recruitId, memberInfo.getMemberId()))
                .build();
    }

    @PostMapping("/{recruitId}/expired")
    public EnvelopeResponse<Void> expiredRecruit(@PathVariable Long recruitId, @Authentication AuthenticatedMember memberInfo) {
        recruitService.expiredRecruit(recruitId, memberInfo.getMemberId());
        return EnvelopeResponse.<Void>builder().build();
    }

    @GetMapping("/{recruitId}")
    public EnvelopeResponse<GetRecruitDetailResDto> getRecruitDetail(@PathVariable Long recruitId, @Authentication AuthenticatedMember memberInfo) {
        return EnvelopeResponse.<GetRecruitDetailResDto>builder()
                .data(recruitService.getRecruitDetail(recruitId, memberInfo.getMemberId()))
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

    @GetMapping("/cursor")
    public EnvelopeResponse<GetRecruitsCursorResDto> getRecruitsByCursor(GetRecruitsReqDto getRecruitsReqDto, Pageable pageable, @Authentication AuthenticatedMember memberInfo) {
        return EnvelopeResponse.<GetRecruitsCursorResDto>builder()
                .data(recruitService.getRecruitsByCursor(getRecruitsReqDto, pageable, memberInfo.getMemberId()))
                .build();
    }

    @GetMapping("/offset")
    public EnvelopeResponse<GetRecruitOffsetResDto> getRecruitsByOffset(GetRecruitsReqDto getRecruitsReqDto, Pageable pageable, @Authentication AuthenticatedMember memberInfo) {
        return EnvelopeResponse.<GetRecruitOffsetResDto>builder()
                .data(recruitService.getRecruitsByOffset(getRecruitsReqDto, pageable, memberInfo.getMemberId()))
                .build();
    }

    @GetMapping("/my-scrap")
    public EnvelopeResponse<GetRecruitsCursorResDto> getScrapedRecruits(Long cursor, Pageable pageable, @Authentication AuthenticatedMember memberInfo) {
        return EnvelopeResponse.<GetRecruitsCursorResDto>builder()
                .data(recruitService.getScrapedRecruits(memberInfo.getMemberId(), cursor, pageable))
                .build();
    }

    @GetMapping("/joined")
    public  EnvelopeResponse<GetRecruitsCursorResDto> getMemberJoinedRecruits(GetMemberJoinRecruitsReqDto getMemberJoinRecruitsReqDto, @Authentication AuthenticatedMember memberInfo) {
        return EnvelopeResponse.<GetRecruitsCursorResDto>builder()
                .data(recruitService.getMemberJoinRecruits(getMemberJoinRecruitsReqDto, memberInfo.getMemberId()))
                .build();
    }

    @GetMapping("/applied")
    public EnvelopeResponse<GetMemberAppliedRecruitsResDto> getMemberAppliedRecruits(GetMemberAppliedRecruitsReqDto recruitsReqDto, @Authentication AuthenticatedMember memberInfo) {
        return EnvelopeResponse.<GetMemberAppliedRecruitsResDto>builder()
                .data(recruitService.getMemberAppliedRecruits(recruitsReqDto, memberInfo.getMemberId()))
                .build();
    }
}
