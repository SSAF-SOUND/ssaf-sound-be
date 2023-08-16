package com.ssafy.ssafsound.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostIdElement {
    private final Long postId;

    public PostIdElement(Long postId) {
        this.postId = postId;
    }
}
