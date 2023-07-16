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

    public static GetCommentElement of(Comment comment, AuthenticatedMember loginMember) {
        Boolean anonymous = comment.getAnonymous();

        return GetCommentElement.builder()
                .content(comment.getContent())
                .parent(comment.getId().equals(comment.getCommentGroup().getId()))
                .likeCount(comment.getLikes().size())
                .createdAt(comment.getCreatedAt())
                .nickname(setNickName(comment, anonymous))
                .anonymous(anonymous)
                .modified(comment.getModifiedAt() != null)
                .liked(isLiked(comment, loginMember))
                .mine(isMine(comment, loginMember))
                .build();
    }

    private static String setNickName(Comment comment, Boolean anonymous) {
        if (anonymous)
            return "익명 " + comment.getCommentNumber().getNumber();
        return comment.getMember().getNickname();
    }

    private static Boolean isLiked(Comment comment, AuthenticatedMember loginMember) {
        if (loginMember == null)
            return false;

        Long loginMemberId = loginMember.getMemberId();
        for (CommentLike like : comment.getLikes()) {
            if (like.getMember().getId().equals(loginMemberId))
                return true;
        }
        return false;
    }

    private static Boolean isMine(Comment comment, AuthenticatedMember loginMember) {
        if (loginMember == null)
            return false;

        return comment.getMember().getId().equals(loginMember.getMemberId());
    }
}
