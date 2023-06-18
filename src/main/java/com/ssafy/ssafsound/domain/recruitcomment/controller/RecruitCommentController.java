package com.ssafy.ssafsound.domain.recruitcomment.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedUser;
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
    public EnvelopeResponse<PostRecruitCommentResDto> saveRecruitComment(@PathVariable Long recruitId, AuthenticatedUser userInfo,
                                                                         @RequestBody PostRecruitCommentReqDto dto) {
        userInfo = AuthenticatedUser.builder().memberId(1L).build();
        return EnvelopeResponse.<PostRecruitCommentResDto>builder()
                .code(String.valueOf(HttpStatus.OK))
                .message("success")
                .data(recruitCommentService.saveRecruitComment(recruitId, userInfo, dto))
                .build();
    }

    @DeleteMapping("/recruit-comments/{recruitCommentId}")
    public EnvelopeResponse<Void> deleteRecruitComment(@PathVariable Long recruitCommentId, AuthenticatedUser userInfo) {
        userInfo = AuthenticatedUser.builder().memberId(1L).build();
        recruitCommentService.deleteRecruitComment(recruitCommentId, userInfo);

        return EnvelopeResponse.<Void>builder().build();
    }

    @PatchMapping("/recruit-comments/{recruitCommentId}")
    public EnvelopeResponse<Void> updateRecruitComment(@PathVariable Long recruitCommentId, AuthenticatedUser userInfo,
                                                       @RequestBody PatchRecruitCommentReqDto dto) {
        userInfo = AuthenticatedUser.builder().memberId(1L).build();
        recruitCommentService.updateRecruitComment(recruitCommentId, userInfo, dto);

        return EnvelopeResponse.<Void>builder().build();
    }
}
