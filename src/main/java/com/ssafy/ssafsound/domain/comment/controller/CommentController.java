package com.ssafy.ssafsound.domain.comment.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.comment.dto.*;
import com.ssafy.ssafsound.domain.comment.service.CommentService;
import com.ssafy.ssafsound.domain.post.dto.PostCommonLikeResDto;
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
    public EnvelopeResponse<CommentIdElement> writeComment(@RequestParam Long postId,
                                                           @Valid @RequestBody PostCommentWriteReqDto postCommentWriteReqDto,
                                                           @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<CommentIdElement>builder()
                .data(commentService.writeComment(postId, loginMember.getMemberId(), postCommentWriteReqDto))
                .build();
    }

    @GetMapping
    public EnvelopeResponse<GetCommentResDto> findComments(@RequestParam Long postId,
                                                           @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<GetCommentResDto>builder()
                .data(commentService.findComments(postId, loginMember))
                .build();
    }

    @PutMapping("/{commentId}")
    public EnvelopeResponse<CommentIdElement> updateComment(@PathVariable Long commentId,
                                                            @Valid @RequestBody PutCommentUpdateReqDto putCommentUpdateReqDto,
                                                            @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<CommentIdElement>builder()
                .data(commentService.updateComment(commentId, loginMember.getMemberId(), putCommentUpdateReqDto))
                .build();
    }

    @PostMapping("/reply")
    public EnvelopeResponse<CommentIdElement> writeCommentReply(@RequestParam Long commentId, @RequestParam Long postId,
                                                                @Valid @RequestBody PostCommentWriteReplyReqDto postCommentWriteReplyReqDto,
                                                                @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<CommentIdElement>builder()
                .data(commentService.writeCommentReply(postId, commentId, loginMember.getMemberId(), postCommentWriteReplyReqDto))
                .build();
    }

    @PostMapping("/{commentId}/like")
    public EnvelopeResponse<PostCommonLikeResDto> likeComment(@PathVariable Long commentId, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<PostCommonLikeResDto>builder()
                .data(commentService.likeComment(commentId, loginMember.getMemberId()))
                .build();
    }

    @DeleteMapping("/{commentId}")
    public EnvelopeResponse<CommentIdElement> deleteComment(@PathVariable Long commentId, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<CommentIdElement>builder()
                .data(commentService.deleteComment(commentId, loginMember.getMemberId()))
                .build();
    }
}
