package com.ssafy.ssafsound.domain.comment.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.comment.dto.GetCommentResDto;
import com.ssafy.ssafsound.domain.comment.dto.PostCommentWriteReqDto;
import com.ssafy.ssafsound.domain.comment.service.CommentService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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

        return EnvelopeResponse.<Long>builder()
                .data(commentService.writeComment(postId, authenticatedMember.getMemberId(), postCommentWriteReqDto))
                .build();
    }

    @GetMapping
    public EnvelopeResponse<GetCommentResDto> findComments(@RequestParam Long postId, Pageable pageable, AuthenticatedMember authenticatedMember) {

        return EnvelopeResponse.<GetCommentResDto>builder()
                .data(commentService.findComments(postId, authenticatedMember, pageable))
                .build();
    }
}
