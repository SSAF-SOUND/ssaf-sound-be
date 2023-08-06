package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetPostResDto {
    private List<GetPostElement> posts;
    private Long cursor;

    public static GetPostResDto of(List<Post> posts, int size) {
        Long nextCursor = null;
        int pageSize = posts.size() - 1;
        if (pageSize >= size) {
            posts = posts.subList(0, pageSize);
            nextCursor = posts.get(posts.size() - 1).getId();
        }

        return GetPostResDto.builder()
                .posts(posts.stream()
                        .map(GetPostElement::from)
                        .collect(Collectors.toList()))
                .cursor(nextCursor)
                .build();

    }
}
