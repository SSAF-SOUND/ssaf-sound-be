package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.comment.domain.Comment;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.post.domain.Post;

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

    public static AuthorElement from(Post post) {
        Member member = post.getMember();
        if (post.getAnonymity())
            return new AuthorElement("익명");
        return new AuthorElement(member);
    }

    private AuthorElement(String nickname) {
        this.nickname = nickname;
    }

    private String setNickName(Comment comment) {
        return "익명 " + comment.getCommentNumber().getNumber();
    }
}
