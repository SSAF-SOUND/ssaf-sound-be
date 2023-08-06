package com.ssafy.ssafsound.domain.post.dto;

import lombok.Getter;

@Getter
public class PostPostLikeResDto {
    private final Integer likeCount;
    private final Boolean liked;

    public PostPostLikeResDto(Integer likeCount, Boolean liked) {
        this.likeCount = likeCount;
        this.liked = liked;
    }
}
