package com.ssafy.ssafsound.domain.comment.dto;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.comment.domain.Comment;
import com.ssafy.ssafsound.domain.comment.domain.CommentLike;
import com.ssafy.ssafsound.domain.member.dto.AuthorElement;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class GetCommentElement {
    private Long commentId;
    private String content;
    private int likeCount;
    private LocalDateTime createdAt;
    private Boolean anonymity;
    private Boolean modified;
    private Boolean liked;
    private Boolean mine;
    private Boolean deletedComment;
    private AuthorElement author;
    private List<GetCommentReplyElement> replies;


    public static GetCommentElement of(Comment comment, AuthenticatedMember loginMember) {
        Boolean anonymity = comment.getAnonymity();

        return GetCommentElement.builder()
                .commentId(comment.getId())
                .content(comment.getDeletedComment() ? "" : comment.getContent())
                .likeCount(comment.getLikes().size())
                .createdAt(comment.getCreatedAt())
                .anonymity(anonymity)
                .modified(comment.getModifiedAt() != null)
                .liked(isLiked(comment, loginMember))
                .mine(isMine(comment, loginMember))
                .deletedComment(comment.getDeletedComment())
                .author(new AuthorElement(comment.getMember(), comment.getAnonymity(), comment.getCommentNumber().getNumber()))
                .replies(new ArrayList<>())
                .build();
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

    public void addReply(GetCommentReplyElement reply) {
        this.replies.add(reply);
    }
}
