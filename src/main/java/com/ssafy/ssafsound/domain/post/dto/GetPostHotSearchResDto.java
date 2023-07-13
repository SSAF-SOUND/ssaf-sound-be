package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.post.domain.HotPost;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetPostHotSearchResDto {
    List<GetPostHotSearchElement> hotPosts;

    public static GetPostHotSearchResDto from(List<HotPost> hotPosts) {
        return GetPostHotSearchResDto.builder()
                .hotPosts(hotPosts.stream()
                        .map(GetPostHotSearchElement::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
