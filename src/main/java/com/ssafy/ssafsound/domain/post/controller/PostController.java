package com.ssafy.ssafsound.domain.post.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.post.dto.*;
import com.ssafy.ssafsound.domain.post.service.PostService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/cursor")
    public EnvelopeResponse<GetPostResDto> findPostsByCursor(@Valid @ModelAttribute GetPostCursorReqDto getPostCursorReqDto) {
        log.info(String.valueOf(getPostCursorReqDto.getCursor()));
        return EnvelopeResponse.<GetPostResDto>builder()
                .data(postService.findPosts(getPostCursorReqDto))
                .build();
    }


    @GetMapping("/{postId}")
    public EnvelopeResponse<GetPostDetailResDto> findPost(@PathVariable Long postId, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<GetPostDetailResDto>builder()
                .data(postService.findPost(postId, loginMember.getMemberId()))
                .build();
    }

    @PostMapping("/{postId}/like")
    public EnvelopeResponse<PostCommonLikeResDto> likePost(@PathVariable Long postId, @Authentication AuthenticatedMember loginMember) {

        return EnvelopeResponse.<PostCommonLikeResDto>builder()
                .data(postService.likePost(postId, loginMember.getMemberId()))
                .build();
    }

    @PostMapping("/{postId}/scrap")
    public EnvelopeResponse<PostPostScrapResDto> scrapPost(@PathVariable Long postId, @Authentication AuthenticatedMember loginMember) {

        return EnvelopeResponse.<PostPostScrapResDto>builder()
                .data(postService.scrapPost(postId, loginMember.getMemberId()))
                .build();
    }

    @PostMapping
    public EnvelopeResponse<PostIdElement> writePost(@Valid @RequestBody PostPostWriteReqDto postPostWriteReqDto,
                                                     @RequestParam Long boardId, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<PostIdElement>builder()
                .data(postService.writePost(boardId, loginMember.getMemberId(), postPostWriteReqDto))
                .build();
    }

    @DeleteMapping("/{postId}")
    public EnvelopeResponse<PostIdElement> deletePost(@PathVariable Long postId, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<PostIdElement>builder()
                .data(postService.deletePost(postId, loginMember.getMemberId()))
                .build();
    }

    @PatchMapping("/{postId}")
    public EnvelopeResponse<PostIdElement> updatePost(@Valid @RequestBody PostPatchUpdateReqDto postPatchUpdateReqDto,
                                                      @PathVariable Long postId, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<PostIdElement>builder()
                .data(postService.updatePost(postId, loginMember.getMemberId(), postPatchUpdateReqDto))
                .build();
    }

    @GetMapping("/hot/cursor")
    public EnvelopeResponse<GetPostResDto> findHotPostsByCursor(@Valid @ModelAttribute GetPostHotReqDto getPostHotReqDto) {
        log.info(String.valueOf(getPostHotReqDto.getCursor()));
        return EnvelopeResponse.<GetPostResDto>builder()
                .data(postService.findHotPosts(getPostHotReqDto))
                .build();
    }

    @GetMapping("/my/cursor")
    public EnvelopeResponse<GetPostResDto> findMyPostsByCursor(@Valid @ModelAttribute GetPostMyReqDto getPostMyReqDto, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<GetPostResDto>builder()
                .data(postService.findMyPosts(getPostMyReqDto, loginMember.getMemberId()))
                .build();
    }

    @GetMapping("/my-scrap/cursor")
    public EnvelopeResponse<GetPostResDto> findMyScrapPostsByCursor(@Valid @ModelAttribute GetPostMyReqDto getPostMyScrapReqDto, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<GetPostResDto>builder()
                .data(postService.findMyScrapPosts(getPostMyScrapReqDto, loginMember.getMemberId()))
                .build();
    }

    @GetMapping("/search/cursor")
    public EnvelopeResponse<GetPostResDto> searchPostsByCursor(@Valid @ModelAttribute GetPostSearchReqDto getPostSearchReqDto) {
        return EnvelopeResponse.<GetPostResDto>builder()
                .data(postService.searchPosts(getPostSearchReqDto))
                .build();
    }

    @GetMapping("/hot/search/cursor")
    public EnvelopeResponse<GetPostResDto> searchHotPostsByCursor(@Valid @ModelAttribute GetPostHotSearchReqDto getPostHotSearchReqDto) {
        return EnvelopeResponse.<GetPostResDto>builder()
                .data(postService.searchHotPosts(getPostHotSearchReqDto))
                .build();
    }
}