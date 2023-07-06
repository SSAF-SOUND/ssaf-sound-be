package com.ssafy.ssafsound.domain.comment.dto;

import com.ssafy.ssafsound.domain.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetCommentElement {
    private String content;
    private Boolean parent;
    private int likeCount;
    private LocalDateTime createdAt;
    private String nickname;
    private Boolean anonymous;
    private Boolean modified;
    private Boolean liked;
    private Boolean mine;

    public static GetCommentElement from(Comment comment, Boolean liked, Boolean mine) {
        Boolean anonymous = comment.getAnonymous();

        return GetCommentElement.builder()
                .content(comment.getContent())
                .parent(comment.getId().equals(comment.getCommentGroup().getId()))
                .likeCount(comment.getLikes().size())
                .createdAt(comment.getCreatedAt())
                .nickname(anonymous ? "익명 " + comment.getCommentNumber().getNumber() : comment.getMember().getNickname())
                .anonymous(anonymous)
                .modified(comment.getModifiedAt() != null)
                .liked(liked)
                .mine(mine)
                .build();
    }
}
