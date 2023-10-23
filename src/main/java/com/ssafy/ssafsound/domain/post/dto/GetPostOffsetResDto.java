package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.post.domain.HotPost;
import com.ssafy.ssafsound.domain.post.domain.Post;
import com.ssafy.ssafsound.domain.post.domain.PostScrap;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetPostOffsetResDto {
    private List<GetPostElement> posts;
    private Integer currentPage;
    private Integer totalPageCount;

    public static GetPostOffsetResDto ofPosts(Page<Post> posts) {
        return GetPostOffsetResDto.builder()
                .posts(posts.getContent().stream()
                        .map(GetPostElement::new)
                        .collect(Collectors.toList()))
                .currentPage(posts.getNumber()+1)
                .totalPageCount(posts.getTotalPages())
                .build();
    }

    public static GetPostOffsetResDto ofHotPosts(Page<HotPost> hotPosts) {
        return GetPostOffsetResDto.builder()
                .posts(hotPosts.getContent().stream()
                        .map(hotPost -> new GetPostElement(hotPost.getPost()))
                        .collect(Collectors.toList()))
                .currentPage(hotPosts.getNumber()+1)
                .totalPageCount(hotPosts.getTotalPages())
                .build();
    }

    public static GetPostOffsetResDto ofPostScraps(Page<PostScrap> postScraps) {
        return GetPostOffsetResDto.builder()
                .posts(postScraps.getContent().stream()
                        .map(postScrap -> new GetPostElement(postScrap.getPost()))
                        .collect(Collectors.toList()))
                .currentPage(postScraps.getNumber()+1)
                .totalPageCount(postScraps.getTotalPages())
                .build();
    }
}
