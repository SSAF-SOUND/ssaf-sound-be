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
        int pageSize = posts.size() - 1;
        if (pageSize >= size) {
            posts = posts.subList(0, pageSize);
            nextCursor = posts.get(pageSize).getId();
        }

        return GetPostMyResDto.builder()
                .posts(posts.stream()
                        .map(GetPostMyElement::from)
                        .collect(Collectors.toList()))
                .cursor(nextCursor)
                .build();
    }
}
