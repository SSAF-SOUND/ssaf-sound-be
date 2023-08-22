package com.ssafy.ssafsound.domain.comment.dto;

import com.ssafy.ssafsound.domain.comment.domain.Comment;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorElement {
    private String nickname;
    private String memberRole;
    private Boolean ssafyMember;
    private Boolean isMajor;
    private SSAFYInfo ssafyInfo;

    public static AuthorElement from(Comment comment) {
        Member member = comment.getMember();
        Boolean anonymity = comment.getAnonymity();

        if (anonymity) {
            return new AuthorElement(comment);
        }
        return new AuthorElement(member);
    }

    private AuthorElement(Comment comment) {
        this.nickname = setNickName(comment);
    }

    private AuthorElement(Member member) {
        this.nickname = member.getNickname();
        this.memberRole = member.getRole().getRoleType();
        this.ssafyMember = member.getSsafyMember();
        this.isMajor = member.getMajor();
        this.ssafyInfo = SSAFYInfo.from(member);
    }

    private String setNickName(Comment comment) {
        return "익명 " + comment.getCommentNumber().getNumber();
    }
}
