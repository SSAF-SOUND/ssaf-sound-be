package com.ssafy.ssafsound.domain.recruitcomment.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitComment;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class RecruitCommentElement {
    private Long recruitCommentId;
    private String content;
    private Long commentGroup;
    private List<RecruitCommentElement> children;
    private Boolean deletedComment;

    private Long memberId;
    private String nickname;
    private Boolean ssafyMember;
    private Boolean major;
    private String majorTrack;

    public static RecruitCommentElement from(RecruitComment comment) {
        Member register = comment.getMember();

        return RecruitCommentElement.builder()
                .recruitCommentId(comment.getId())
                .content(!comment.getDeletedComment() ? comment.getContent() : "")
                .commentGroup(comment.getCommentGroup().getId())
                .children(new ArrayList<>())
                .deletedComment(comment.getDeletedComment())
                .memberId(register.getId())
                .nickname(register.getNickname())
                .ssafyMember(register.getSsafyMember())
                .major(register.getMajor())
                .majorTrack(register.getMajorTrack().getName())
                .build();
    }

    public void addChild(RecruitCommentElement child) {
        this.children.add(child);
    }
}
