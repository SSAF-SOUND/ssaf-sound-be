package com.ssafy.ssafsound.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCommonLikeResDto {
    private final Integer likeCount;
    private final Boolean liked;

    public PostCommonLikeResDto(Integer likeCount, Boolean liked) {
        this.likeCount = likeCount;
        this.liked = liked;
    }
}
