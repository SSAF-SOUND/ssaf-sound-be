package com.ssafy.ssafsound.domain.comment.dto;

import com.ssafy.ssafsound.domain.comment.domain.Comment;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;
import lombok.Getter;

@Getter
public class AuthorElement {
    private final String nickname;
    private String memberRole;
    private Boolean ssafyMember;
    private Boolean isMajor;
    private SSAFYInfo ssafyInfo;

    public static AuthorElement from(Comment comment) {
        Member member = comment.getMember();
        Boolean anonymity = comment.getAnonymity();

        if (comment.getAnonymity())
            return new AuthorElement(comment, anonymity);
        return new AuthorElement(member);
    }

    private AuthorElement(Comment comment, Boolean anonymity) {
        this.nickname = setNickName(comment, anonymity);
    }

    private AuthorElement(Member member) {
        this.nickname = member.getNickname();
        this.memberRole = member.getRole().getRoleType();
        this.ssafyMember = member.getSsafyMember();
        this.isMajor = member.getMajor();
        this.ssafyInfo = SSAFYInfo.from(member);
    }

    private String setNickName(Comment comment, Boolean anonymity) {
        if (anonymity)
            return "익명 " + comment.getCommentNumber().getNumber();
        return comment.getMember().getNickname();
    }
}
