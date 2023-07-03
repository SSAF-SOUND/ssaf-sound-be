package com.ssafy.ssafsound.domain.comment.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.comment.dto.PostCommentWriteReqDto;
import com.ssafy.ssafsound.domain.comment.service.CommentService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public EnvelopeResponse<Long> writeComment(@RequestParam Long postId,
                                               @Valid @RequestBody PostCommentWriteReqDto postCommentWriteReqDto,
                                               AuthenticatedMember authenticatedMember) {
        AuthenticatedMember member = AuthenticatedMember.builder()
                .memberId(1L)
                .memberRole("user")
                .build();


        return EnvelopeResponse.<Long>builder()
                .data(commentService.writeComment(postId, member.getMemberId(), postCommentWriteReqDto))
                .build();
    }
}
