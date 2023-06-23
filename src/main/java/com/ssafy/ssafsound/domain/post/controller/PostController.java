package com.ssafy.ssafsound.domain.post.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.post.dto.GetPostDetailListResDto;
import com.ssafy.ssafsound.domain.post.dto.GetPostListResDto;
import com.ssafy.ssafsound.domain.post.dto.PostPostWriteReqDto;
import com.ssafy.ssafsound.domain.post.service.PostService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public EnvelopeResponse<GetPostListResDto> findPosts(@RequestParam Long boardId, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        return EnvelopeResponse.<GetPostListResDto>builder()
                .data(postService.findPosts(boardId, pageRequest))
                .build();
    }

    @GetMapping("/{postId}")
    public EnvelopeResponse<GetPostDetailListResDto> findPost(AuthenticatedMember authenticatedMember, @PathVariable Long postId) {
        AuthenticatedMember testMember = AuthenticatedMember.builder()
                .memberId(1L)
                .memberRole("NORMAL")
                .build();

        return EnvelopeResponse.<GetPostDetailListResDto>builder()
                .data(postService.findPost(postId, testMember))
                .build();
    }

    @PostMapping("/{postId}/like")
    public EnvelopeResponse<Void> postLike(AuthenticatedMember authenticatedMember, @PathVariable Long postId) {
        AuthenticatedMember testMember = AuthenticatedMember.builder()
                .memberId(1L)
                .memberRole("NORMAL")
                .build();

        postService.postLike(postId, testMember.getMemberId());
        return EnvelopeResponse.<Void>builder()
                .build();
    }

    @PostMapping("/{postId}/scrap")
    public EnvelopeResponse<Void> postScrap(AuthenticatedMember authenticatedMember, @PathVariable Long postId) {
        AuthenticatedMember testMember = AuthenticatedMember.builder()
                .memberId(1L)
                .memberRole("NORMAL")
                .build();

        postService.postScrap(postId, testMember.getMemberId());
        return EnvelopeResponse.<Void>builder()
                .build();
    }

    @PostMapping
    public EnvelopeResponse writePost(@RequestPart("data") PostPostWriteReqDto postPostWriteReqDto, @RequestPart List<MultipartFile> images,
                                      @RequestParam Long boardId, AuthenticatedMember authenticatedMember) {
        AuthenticatedMember testMember = AuthenticatedMember.builder()
                .memberId(1L)
                .memberRole("NORMAL")
                .build();

        log.info("글쓰기 컨트롤러");
        log.info("boardId: " + boardId);
        log.info("제목 : " + postPostWriteReqDto.getTitle());
        log.info("내용 : " + postPostWriteReqDto.getContent());
        log.info("익명 : " + postPostWriteReqDto.isAnonymous());
        for (MultipartFile image : images) {
            log.info("이미지 : " + image.getOriginalFilename());
        }

        return EnvelopeResponse.builder()
                .data(postService.writePost(boardId, postPostWriteReqDto, images, testMember.getMemberId()))
                .build();
    }
}