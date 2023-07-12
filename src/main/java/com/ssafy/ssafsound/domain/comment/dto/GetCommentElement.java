package com.ssafy.ssafsound.domain.comment.dto;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.comment.domain.Comment;
import com.ssafy.ssafsound.domain.comment.domain.CommentLike;
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

    public static GetCommentElement of(Comment comment, AuthenticatedMember member) {
        Boolean anonymous = comment.getAnonymous();

        return GetCommentElement.builder()
                .content(comment.getContent())
                .parent(comment.getId().equals(comment.getCommentGroup().getId()))
                .likeCount(comment.getLikes().size())
                .createdAt(comment.getCreatedAt())
                .nickname(anonymous ? "익명 " + comment.getCommentNumber().getNumber() : comment.getMember().getNickname())
                .anonymous(anonymous)
                .modified(comment.getModifiedAt() != null)
                .liked(isLiked(comment, member))
                .mine(isMine(comment, member))
                .build();
    }

    private static Boolean isLiked(Comment comment, AuthenticatedMember member) {
        if (member == null)
            return false;

        Long memberId = member.getMemberId();
        for (CommentLike like : comment.getLikes()) {
            if (like.getMember().getId().equals(memberId))
                return true;
        }
        return false;
    }

    private static Boolean isMine(Comment comment, AuthenticatedMember member) {
        if (member == null)
            return false;

        return comment.getMember().getId().equals(member.getMemberId());
    }
}
