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
    public EnvelopeResponse<GetPostCursorResDto> findPostsByCursor(@Valid @ModelAttribute GetPostCursorReqDto getPostCursorReqDto) {
        return EnvelopeResponse.<GetPostCursorResDto>builder()
                .data(postService.findPostsByCursor(getPostCursorReqDto))
                .build();
    }

    @GetMapping("/offset")
    public EnvelopeResponse<GetPostOffsetResDto> findPostsByOffset(@Valid @ModelAttribute GetPostOffsetReqDto getPostOffsetReqDto) {
        return EnvelopeResponse.<GetPostOffsetResDto>builder()
                .data(postService.findPostsByOffset(getPostOffsetReqDto))
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
    public EnvelopeResponse<GetPostCursorResDto> findHotPostsByCursor(@Valid @ModelAttribute GetPostHotCursorReqDto getPostHotCursorReqDto) {
        return EnvelopeResponse.<GetPostCursorResDto>builder()
                .data(postService.findHotPostsByCursor(getPostHotCursorReqDto))
                .build();
    }

    @GetMapping("/hot/offset")
    public EnvelopeResponse<GetPostOffsetResDto> findHotPostsByOffset(@Valid @ModelAttribute BasePageRequest basePageRequest) {
        return EnvelopeResponse.<GetPostOffsetResDto>builder()
                .data(postService.findHotPostsByOffset(basePageRequest))
                .build();
    }


    @GetMapping("/my/cursor")
    public EnvelopeResponse<GetPostCursorResDto> findMyPostsByCursor(@Valid @ModelAttribute GetPostMyReqDto getPostMyReqDto, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<GetPostCursorResDto>builder()
                .data(postService.findMyPostsByCursor(getPostMyReqDto, loginMember.getMemberId()))
                .build();
    }

    @GetMapping("/my-scrap/cursor")
    public EnvelopeResponse<GetPostCursorResDto> findMyScrapPostsByCursor(@Valid @ModelAttribute GetPostMyReqDto getPostMyScrapReqDto, @Authentication AuthenticatedMember loginMember) {
        return EnvelopeResponse.<GetPostCursorResDto>builder()
                .data(postService.findMyScrapPostsByCursor(getPostMyScrapReqDto, loginMember.getMemberId()))
                .build();
    }

    @GetMapping("/search/cursor")
    public EnvelopeResponse<GetPostCursorResDto> searchPostsByCursor(@Valid @ModelAttribute GetPostSearchCursorReqDto getPostSearchCursorReqDto) {
        return EnvelopeResponse.<GetPostCursorResDto>builder()
                .data(postService.searchPostsByCursor(getPostSearchCursorReqDto))
                .build();
    }

    @GetMapping("/search/offset")
    public EnvelopeResponse<GetPostOffsetResDto> searchPostsByOffset(@Valid @ModelAttribute GetPostSearchOffsetReqDto getPostSearchOffsetReqDto) {
        return EnvelopeResponse.<GetPostOffsetResDto>builder()
                .data(postService.searchPostsByOffset(getPostSearchOffsetReqDto))
                .build();
    }

    @GetMapping("/hot/search/cursor")
    public EnvelopeResponse<GetPostCursorResDto> searchHotPostsByCursor(@Valid @ModelAttribute GetPostHotSearchReqDto getPostHotSearchReqDto) {
        return EnvelopeResponse.<GetPostCursorResDto>builder()
                .data(postService.searchHotPostsByCursor(getPostHotSearchReqDto))
                .build();
    }
}