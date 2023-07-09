package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetPostSearchResDto {
    private List<GetPostSearchElement> posts;

    public static GetPostSearchResDto from(List<Post> posts) {
        return GetPostSearchResDto.builder()
                .posts(posts.stream()
                        .map(GetPostSearchElement::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
