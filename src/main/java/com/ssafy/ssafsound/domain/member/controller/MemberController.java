package com.ssafy.ssafsound.domain.member.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.member.dto.GetMemberResDto;
import com.ssafy.ssafsound.domain.member.dto.PostMemberInfoReqDto;
import com.ssafy.ssafsound.domain.member.dto.PostNicknameReqDto;
import com.ssafy.ssafsound.domain.member.dto.PostNicknameResDto;
import com.ssafy.ssafsound.domain.member.service.MemberService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
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

    @PatchMapping
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
}
