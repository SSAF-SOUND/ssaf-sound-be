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

    public static GetPostHotResDto of(List<HotPost> hotPosts, int size) {
        Long nextCursor = null;
        int pageSize = hotPosts.size() - 1;
        if (pageSize >= size) {
            hotPosts = hotPosts.subList(0, pageSize);
            nextCursor = hotPosts.get(hotPosts.size() - 1).getId();
        }

        return GetPostHotResDto.builder()
                .posts(hotPosts.stream()
                        .map(GetPostHotElement::from)
                        .collect(Collectors.toList()))
                .cursor(nextCursor)
                .build();
    }
}
