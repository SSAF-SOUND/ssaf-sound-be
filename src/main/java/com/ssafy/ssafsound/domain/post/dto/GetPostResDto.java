package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetPostResDto {
    private List<GetPost> posts;

    public static GetPostResDto from(List<Post> posts) {
        return GetPostResDto.builder()
                .posts(posts.stream().map(GetPost::from).collect(Collectors.toList()))
                .build();

    }
}
