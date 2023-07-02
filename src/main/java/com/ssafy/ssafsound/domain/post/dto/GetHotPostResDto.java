package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.post.domain.HotPost;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetHotPostResDto {
    private List<GetHotPostElement> posts;

    public static GetHotPostResDto from(List<HotPost> hotPosts) {
        return GetHotPostResDto.builder()
                .posts(hotPosts
                        .stream()
                        .map(GetHotPostElement::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
