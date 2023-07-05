package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetPostMyResDto {
    private List<GetPostMyElement> posts;

    public static GetPostMyResDto from(List<Post> posts) {
        return GetPostMyResDto.builder()
                .posts(posts.stream()
                        .map(GetPostMyElement::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
