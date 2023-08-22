package com.ssafy.ssafsound.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentIdElement {
    private final Long commentId;

    public CommentIdElement(Long commentId) {
        this.commentId = commentId;
    }
}
