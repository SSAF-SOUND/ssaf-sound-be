package com.ssafy.ssafsound.domain.member.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.member.dto.*;
import com.ssafy.ssafsound.domain.member.service.MemberService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public EnvelopeResponse<GetMemberResDto> getMemberInformation(@Authentication AuthenticatedMember authenticatedMember) {
        return EnvelopeResponse.<GetMemberResDto>builder()
                .data(memberService.getMemberInformation(authenticatedMember))
                .build();
    }

    @GetMapping("/{memberId}/portfolio")
    public EnvelopeResponse<GetMemberPortfolioResDto> getMemberPortfolioById(@PathVariable Long memberId) {
        return EnvelopeResponse.<GetMemberPortfolioResDto>builder()
                .data(memberService.getMemberPortfolioById(memberId))
                .build();
    }

    @GetMapping("/{memberId}/profile")
    public EnvelopeResponse<GetMemberProfileResDto> getMemberProfileById(@PathVariable Long memberId) {
        return EnvelopeResponse.<GetMemberProfileResDto>builder()
                .data(memberService.getMemberProfileById(memberId))
                .build();
    }

    @PutMapping
    public EnvelopeResponse<GetMemberResDto> registerMemberInformation(@Authentication AuthenticatedMember authenticatedMember,
                                                                       @Valid @RequestBody PostMemberInfoReqDto postMemberInfoReqDto) {
        return EnvelopeResponse.<GetMemberResDto>builder()
                .data(memberService.registerMemberInformation(authenticatedMember, postMemberInfoReqDto))
                .build();
    }

    @PostMapping("/nickname")
    public EnvelopeResponse<PostNicknameResDto> checkNicknamePossible(@Valid @RequestBody PostNicknameReqDto postNicknameReqDto) {
        return EnvelopeResponse.<PostNicknameResDto>builder()
                .data(memberService.checkNicknamePossible(postNicknameReqDto))
                .build();
    }

    @PostMapping("/ssafy-certification")
    public EnvelopeResponse<PostCertificationInfoResDto> certifySSAFYInformation(@Authentication AuthenticatedMember authenticatedMember,
                                                                                @Valid @RequestBody PostCertificationInfoReqDto postCertificationInfoReqDto) {
        return EnvelopeResponse.<PostCertificationInfoResDto>builder()
                .data(memberService.certifySSAFYInformation(authenticatedMember, postCertificationInfoReqDto))
                .build();
    }

    @PutMapping("/portfolio")
    public EnvelopeResponse registerMemberPortfolio(@Authentication AuthenticatedMember authenticatedMember,
                                                 @Valid @RequestBody PutMemberProfileReqDto putMemberProfileReqDto) {
        memberService.registerMemberPortfolio(authenticatedMember, putMemberProfileReqDto);
        return EnvelopeResponse.builder()
                .build();
    }

    @PatchMapping("/default-information")
    public EnvelopeResponse patchMemberDefaultInformation(
            @Authentication AuthenticatedMember authenticatedMember,
            @Valid @RequestBody PatchMemberDefaultInfoReqDto patchMemberDefaultInfoReqDto) {
        memberService.patchMemberDefaultInf();
        return EnvelopeResponse.builder()
                .build();
    }
}
