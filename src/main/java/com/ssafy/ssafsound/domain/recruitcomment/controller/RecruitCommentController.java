package com.ssafy.ssafsound.domain.recruitcomment.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PatchRecruitCommentReqDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentReqDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentResDto;
import com.ssafy.ssafsound.domain.recruitcomment.service.RecruitCommentService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RecruitCommentController {

    private final RecruitCommentService recruitCommentService;

    @PostMapping("/recruits/{recruitId}/comments")
    public EnvelopeResponse<PostRecruitCommentResDto> saveRecruitComment(@PathVariable Long recruitId, AuthenticatedMember memberInfo,
                                                                         @RequestBody PostRecruitCommentReqDto dto) {
        return EnvelopeResponse.<PostRecruitCommentResDto>builder()
                .code(String.valueOf(HttpStatus.OK))
                .message("success")
                .data(recruitCommentService.saveRecruitComment(recruitId, memberInfo, dto))
                .build();
    }

    @DeleteMapping("/recruit-comments/{recruitCommentId}")
    public EnvelopeResponse<Void> deleteRecruitComment(@PathVariable Long recruitCommentId, AuthenticatedMember memberInfo) {
        recruitCommentService.deleteRecruitComment(recruitCommentId, memberInfo);
        return EnvelopeResponse.<Void>builder().build();
    }

    @PatchMapping("/recruit-comments/{recruitCommentId}")
    public EnvelopeResponse<Void> updateRecruitComment(@PathVariable Long recruitCommentId, AuthenticatedMember memberInfo,
                                                       @RequestBody PatchRecruitCommentReqDto dto) {
        recruitCommentService.updateRecruitComment(recruitCommentId, memberInfo, dto);
        return EnvelopeResponse.<Void>builder().build();
    }

    @PostMapping("/recruit-comments/{recruitCommentId}/like")
    public EnvelopeResponse<Void> toggleRecruitCommentLike(@PathVariable Long recruitCommentId, AuthenticatedMember memberInfo) {
        recruitCommentService.toggleRecruitCommentLike(recruitCommentId, memberInfo.getMemberId());
        return EnvelopeResponse.<Void>builder().build();
    }
}
