package com.ssafy.ssafsound.domain.post.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.post.dto.GetPostDetailListResDto;
import com.ssafy.ssafsound.domain.post.dto.GetPostListResDto;
import com.ssafy.ssafsound.domain.post.dto.GetPostReqDto;
import com.ssafy.ssafsound.domain.post.service.PostService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
    public EnvelopeResponse<GetPostDetailListResDto> findPost(@Authentication AuthenticatedMember authenticatedMember, @PathVariable Long postId) {
//        AuthenticatedMember authenticatedMember = AuthenticatedMember.builder()
//                .memberId(1L)
//                .memberRole("NORMAL")
//                .build();
//
//        log.info("사용자 정보 : " + authenticatedMember.getMemberId() + " / " + authenticatedMember.getMemberRole());
        return EnvelopeResponse.<GetPostDetailListResDto>builder()
                .data(postService.findPost(postId, authenticatedMember))
                .build();
    }
}