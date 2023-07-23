package com.ssafy.ssafsound.domain.post.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.post.dto.GetPostDetailResDto;
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
    public EnvelopeResponse<GetPostResDto> findPosts(@Valid GetPostReqDto getPostReqDto) {
        return EnvelopeResponse.<GetPostResDto>builder()
                .data(postService.findPosts(getPostReqDto.getBoardId(), getPostReqDto.getCursor(), getPostReqDto.getSize()))
                .build();
    }

    @GetMapping("/{postId}")
    public EnvelopeResponse<GetPostDetailResDto> findPost(@PathVariable Long postId, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<GetPostDetailResDto>builder()
                .data(postService.findPost(postId, loginMember.getMemberId()))
                .build();
    }

    @PostMapping("/{postId}/like")
    public EnvelopeResponse<Void> likePost(@PathVariable Long postId, @Authentication AuthenticatedMember loginMember) {
        postService.likePost(postId, loginMember.getMemberId());

        return EnvelopeResponse.<Void>builder()
                .build();
    }

    @PostMapping("/{postId}/scrap")
    public EnvelopeResponse<Void> scrapPost(@PathVariable Long postId, @Authentication AuthenticatedMember loginMember) {
        postService.scrapPost(postId, loginMember.getMemberId());

        return EnvelopeResponse.<Void>builder()
                .build();
    }

    @PostMapping("/{postId}/report")
    public EnvelopeResponse<Long> reportPost(@PathVariable Long postId,
                                             @Valid @RequestBody PostPostReportReqDto postPostReportReqDto,
                                             @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<Long>builder()
                .data(postService.reportPost(postId, loginMember.getMemberId(), postPostReportReqDto.getContent()))
                .build();
    }

    @PostMapping
    public EnvelopeResponse<Long> writePost(@Valid @RequestBody PostPostWriteReqDto postPostWriteReqDto,
                                            @RequestParam Long boardId, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<Long>builder()
                .data(postService.writePost(boardId, loginMember.getMemberId(), postPostWriteReqDto))
                .build();
    }

    @DeleteMapping("/{postId}")
    public EnvelopeResponse<Long> deletePost(@PathVariable Long postId, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<Long>builder()
                .data(postService.deletePost(postId, loginMember.getMemberId()))
                .build();
    }

    @PutMapping("/{postId}")
    public EnvelopeResponse<Long> updatePost(@Valid @RequestBody PostPutUpdateReqDto postPutUpdateReqDto,
                                             @PathVariable Long postId, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<Long>builder()
                .data(postService.updatePost(postId, loginMember.getMemberId(), postPutUpdateReqDto))
                .build();
    }

    @GetMapping("/hot")
    public EnvelopeResponse<GetPostHotResDto> findHotPosts(@Valid GetPostHotReqDto getPostHotReqDto) {
        return EnvelopeResponse.<GetPostHotResDto>builder()
                .data(postService.findHotPosts(getPostHotReqDto.getCursor(), getPostHotReqDto.getSize()))
                .build();
    }

    @GetMapping("/my")
    public EnvelopeResponse<GetPostMyResDto> findMyPosts(Pageable pageable, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<GetPostMyResDto>builder()
                .data(postService.findMyPosts(pageable, loginMember.getMemberId()))
                .build();
    }

    @GetMapping("/search")
    public EnvelopeResponse<GetPostResDto> searchPosts(@Valid GetPostSearchReqDto getPostSearchReqDto) {
        return EnvelopeResponse.<GetPostResDto>builder()
                .data(postService.searchPosts(getPostSearchReqDto.getBoardId(), getPostSearchReqDto.getKeyword(),
                        getPostSearchReqDto.getCursor(), getPostSearchReqDto.getSize()))
                .build();
    }

    @GetMapping("/hot/search")
    public EnvelopeResponse<GetPostHotResDto> searchHotPosts(@Valid GetPostHotSearchReqDto getPostHotSearchReqDto) {
        return EnvelopeResponse.<GetPostHotResDto>builder()
                .data(postService.searchHotPosts(getPostHotSearchReqDto.getKeyword(), getPostHotSearchReqDto.getCursor(), getPostHotSearchReqDto.getSize()))
                .build();
    }
}