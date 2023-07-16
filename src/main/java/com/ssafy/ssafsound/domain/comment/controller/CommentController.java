package com.ssafy.ssafsound.domain.comment.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.comment.dto.GetCommentResDto;
import com.ssafy.ssafsound.domain.comment.dto.PostCommentWriteReplyReqDto;
import com.ssafy.ssafsound.domain.comment.dto.PostCommentWriteReqDto;
import com.ssafy.ssafsound.domain.comment.dto.PutCommentUpdateReqDto;
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
                                               @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<Long>builder()
                .data(commentService.writeComment(postId, loginMember.getMemberId(), postCommentWriteReqDto))
                .build();
    }

    @GetMapping
    public EnvelopeResponse<GetCommentResDto> findComments(@RequestParam Long postId, Pageable pageable,
                                                           @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<GetCommentResDto>builder()
                .data(commentService.findComments(postId, loginMember, pageable))
                .build();
    }

    @PutMapping("/{commentId}")
    public EnvelopeResponse<Long> updateComment(@PathVariable Long commentId,
                                                @Valid @RequestBody PutCommentUpdateReqDto putCommentUpdateReqDto,
                                                @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<Long>builder()
                .data(commentService.updateComment(commentId, loginMember.getMemberId(), putCommentUpdateReqDto))
                .build();
    }

    @PostMapping("/reply")
    public EnvelopeResponse<Long> writeCommentReply(@RequestParam Long commentId, @RequestParam Long postId,
                                                    @Valid @RequestBody PostCommentWriteReplyReqDto postCommentWriteReplyReqDto,
                                                    @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<Long>builder()
                .data(commentService.writeCommentReply(postId, commentId, loginMember.getMemberId(), postCommentWriteReplyReqDto))
                .build();
    }

    @PostMapping("/{commentId}/like")
    public EnvelopeResponse<Long> likeComment(@PathVariable Long commentId, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<Long>builder()
                .data(commentService.likeComment(commentId, loginMember.getMemberId()))
                .build();
    }

    @DeleteMapping("/{commentId}")
    public EnvelopeResponse<Long> deleteComment(@PathVariable Long commentId, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<Long>builder()
                .data(commentService.deleteComment(commentId, loginMember.getMemberId()))
                .build();
    }
}
