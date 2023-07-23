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
    private Long cursor;

    public static GetPostMyResDto of(List<Post> posts, int size) {
        Long nextCursor = null;
        if (posts.size() > size) {
            posts = posts.subList(0, posts.size() - 1);
            nextCursor = posts.get(posts.size() - 1).getId();
        }

        return GetPostMyResDto.builder()
                .posts(posts.stream()
                        .map(GetPostMyElement::from)
                        .collect(Collectors.toList()))
                .cursor(nextCursor)
                .build();
    }
}
