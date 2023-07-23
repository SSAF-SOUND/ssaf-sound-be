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
    private Long cursor;

    public static GetPostHotResDto from(List<HotPost> hotPosts, int size) {
        Long nextCursor = null;
        if (hotPosts.size() > size) {
            hotPosts = hotPosts.subList(0, hotPosts.size() - 1);
            nextCursor = hotPosts.get(hotPosts.size() - 1).getId();
        }

        return GetPostHotResDto.builder()
                .posts(hotPosts
                        .stream()
                        .map(GetPostHotElement::from)
                        .collect(Collectors.toList()))
                .cursor(nextCursor)
                .build();
    }
}
