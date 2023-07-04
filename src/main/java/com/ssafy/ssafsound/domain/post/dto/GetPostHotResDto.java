package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.post.domain.HotPost;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetPostHotResDto {
    private List<GetPostHotElement> posts;

    public static GetPostHotResDto from(List<HotPost> hotPosts) {
        return GetPostHotResDto.builder()
                .posts(hotPosts
                        .stream()
                        .map(GetPostHotElement::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
