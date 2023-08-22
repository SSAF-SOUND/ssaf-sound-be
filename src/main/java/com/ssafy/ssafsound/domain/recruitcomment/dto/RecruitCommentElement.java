package com.ssafy.ssafsound.domain.recruitcomment.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.dto.AuthorElement;
import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class RecruitCommentElement {
    private Long commentId;
    private String content;
    private Long commentGroup;
    private int likeCount;
    private Boolean liked;
    private Boolean mine;
    private LocalDateTime createdAt;
    private Boolean deletedComment;
    private Boolean modified;
    private AuthorElement author;
    private List<RecruitCommentElement> replies;

    public static RecruitCommentElement of(RecruitComment comment, int likedCount, Boolean liked, Boolean mine) {
        Member register = comment.getMember();

        return RecruitCommentElement.builder()
                .commentId(comment.getId())
                .content(!comment.getDeletedComment() ? comment.getContent() : "")
                .commentGroup(comment.getCommentGroup().getId())
                .likeCount(likedCount)
                .liked(liked)
                .mine(mine)
                .replies(new ArrayList<>())
                .deletedComment(comment.getDeletedComment())
                .createdAt(comment.getCreatedAt())
                .modified(comment.getModifiedAt() != null)
                .author(new AuthorElement(register, false))
                .build();
    }

    public void addChild(RecruitCommentElement child) {
        this.replies.add(child);
    }
}
