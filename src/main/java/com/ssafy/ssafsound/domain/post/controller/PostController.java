package com.ssafy.ssafsound.domain.post.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.post.dto.GetPostDetailListResDto;
import com.ssafy.ssafsound.domain.post.dto.GetPostResDto;
import com.ssafy.ssafsound.domain.post.dto.PostPostReportReqDto;
import com.ssafy.ssafsound.domain.post.dto.PostPostWriteReqDto;
import com.ssafy.ssafsound.domain.post.dto.*;
import com.ssafy.ssafsound.domain.post.service.PostService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public EnvelopeResponse<GetPostResDto> findPosts(@RequestParam Long boardId, Pageable pageable) {

        return EnvelopeResponse.<GetPostResDto>builder()
                .data(postService.findPosts(boardId, pageable))
                .build();
    }

    @GetMapping("/{postId}")
    public EnvelopeResponse<GetPostDetailListResDto> findPost(AuthenticatedMember authenticatedMember, @PathVariable Long postId) {
        return EnvelopeResponse.<GetPostDetailListResDto>builder()
                .data(postService.findPost(postId, authenticatedMember))
                .build();
    }

    @PostMapping("/{postId}/like")
    public EnvelopeResponse<Void> postLike(AuthenticatedMember authenticatedMember, @PathVariable Long postId) {
        postService.postLike(postId, authenticatedMember.getMemberId());

        return EnvelopeResponse.<Void>builder()
                .build();
    }

    @PostMapping("/{postId}/scrap")
    public EnvelopeResponse<Void> postScrap(AuthenticatedMember authenticatedMember, @PathVariable Long postId) {
        postService.postScrap(postId, authenticatedMember.getMemberId());

        return EnvelopeResponse.<Void>builder()
                .build();
    }

    @PostMapping("/{postId}/report")
    public EnvelopeResponse<Long> reportPost(AuthenticatedMember authenticatedMember, @PathVariable Long postId,
                                             @Valid @RequestBody PostPostReportReqDto postPostReportReqDto) {

        return EnvelopeResponse.<Long>builder()
                .data(postService.reportPost(postId, authenticatedMember.getMemberId(), postPostReportReqDto.getContent()))
                .build();
    }

    @PostMapping
    public EnvelopeResponse<Long> writePost(@Valid @ModelAttribute PostPostWriteReqDto postPostWriteReqDto,
                                            @RequestParam Long boardId, AuthenticatedMember authenticatedMember) {

        return EnvelopeResponse.<Long>builder()
                .data(postService.writePost(boardId, authenticatedMember.getMemberId(), postPostWriteReqDto, postPostWriteReqDto.getImages()))
                .build();
    }

    @DeleteMapping("/{postId}")
    public EnvelopeResponse<Long> deletePost(AuthenticatedMember authenticatedMember, @PathVariable Long postId) {

        return EnvelopeResponse.<Long>builder()
                .data(postService.deletePost(postId, authenticatedMember.getMemberId()))
                .build();
    }

    @PutMapping("/{postId}")
    public EnvelopeResponse<Long> updatePost(@Valid @ModelAttribute PostPutUpdateReqDto postPutUpdateReqDto,
                                             @PathVariable Long postId, AuthenticatedMember authenticatedMember) {

        return EnvelopeResponse.<Long>builder()
                .data(postService.updatePost(postId, authenticatedMember.getMemberId(), postPutUpdateReqDto))
                .build();
    }

    @GetMapping("/hot")
    public EnvelopeResponse<GetPostHotResDto> findHotPosts(Pageable pageable) {

        return EnvelopeResponse.<GetPostHotResDto>builder()
                .data(postService.findHotPosts(pageable))
                .build();
    }

    @GetMapping("/my")
    public EnvelopeResponse<GetPostMyResDto> findMyPosts(Pageable pageable, AuthenticatedMember authenticatedMember) {

        return EnvelopeResponse.<GetPostMyResDto>builder()
                .data(postService.findMyPosts(pageable, authenticatedMember.getMemberId()))
                .build();
    }

    @GetMapping("/search")
    public EnvelopeResponse<GetPostSearchResDto> searchPosts(@Valid GetPostSearchReqDto getPostSearchReqDto, Pageable pageable) {

        return EnvelopeResponse.<GetPostSearchResDto>builder()
                .data(postService.searchPosts(getPostSearchReqDto.getBoardId(), getPostSearchReqDto.getKeyword(), pageable))
                .build();
    }

    @GetMapping("/hot/search")
    public EnvelopeResponse<GetPostHotSearchResDto> searchHotPosts(@Valid GetPostHotSearchReqDto getPostHotSearchReqDto, Pageable pageable) {

        return EnvelopeResponse.<GetPostHotSearchResDto>builder()
                .data(postService.searchHotPosts(getPostHotSearchReqDto.getKeyword(), pageable))
                .build();
    }
}