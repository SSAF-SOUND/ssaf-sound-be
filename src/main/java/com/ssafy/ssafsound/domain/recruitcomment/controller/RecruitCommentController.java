package com.ssafy.ssafsound.domain.recruitcomment.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedUser;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentReqDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentResDto;
import com.ssafy.ssafsound.domain.recruitcomment.service.RecruitCommentService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecruitCommentController {

    private final RecruitCommentService recruitCommentService;

    @PostMapping("/recruits/{recruitId}/comments")
    public EnvelopeResponse<PostRecruitCommentResDto> saveRecruitComment(@PathVariable Long recruitId, AuthenticatedUser userInfo,
                                                                         @RequestBody PostRecruitCommentReqDto dto) {

        return EnvelopeResponse.<PostRecruitCommentResDto>builder()
                .code(String.valueOf(HttpStatus.OK))
                .message("success")
                .data(recruitCommentService.saveRecruitComment(recruitId, userInfo, dto))
                .build();
    }
}
