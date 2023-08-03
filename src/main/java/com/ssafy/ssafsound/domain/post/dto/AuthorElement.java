package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;
import com.ssafy.ssafsound.domain.post.domain.Post;
import lombok.Getter;

@Getter
public class AuthorElement {
    private final String nickname;
    private String memberRole;
    private Boolean ssafyMember;
    private Boolean isMajor;
    private SSAFYInfo ssafyInfo;

    public static AuthorElement from(Post post) {
        Member member = post.getMember();
        if (post.getAnonymous())
            return new AuthorElement("익명");
        return new AuthorElement(member);
    }

    private AuthorElement(String nickname) {
        this.nickname = nickname;
    }

    private AuthorElement(Member member) {
        this.nickname = member.getNickname();
        this.memberRole = member.getRole().getRoleType();
        this.ssafyMember = member.getSsafyMember();
        this.isMajor = member.getMajor();
        this.ssafyInfo = SSAFYInfo.from(member);
    }
}
