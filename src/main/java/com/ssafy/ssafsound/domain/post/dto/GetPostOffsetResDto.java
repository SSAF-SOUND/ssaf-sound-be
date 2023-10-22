package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.post.domain.HotPost;
import com.ssafy.ssafsound.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetPostOffsetResDto {
    private List<GetPostElement> posts;

    public static GetPostOffsetResDto ofPosts(List<Post> posts) {
        return GetPostOffsetResDto.builder()
                .posts(posts.stream()
                        .map(GetPostElement::new)
                        .collect(Collectors.toList()))
                .build();
    }

    public static GetPostOffsetResDto ofHotPosts(List<HotPost> hotPosts) {
        return GetPostOffsetResDto.builder()
                .posts(hotPosts.stream()
                        .map(hotPost -> new GetPostElement(hotPost.getPost()))
                        .collect(Collectors.toList()))
                .build();
    }
}
