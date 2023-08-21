package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;

import lombok.Getter;

@Getter
public class AuthorElement {

    private Long memberId;
    private String nickname;
    private String memberRole;
    private Boolean ssafyMember;
    private Boolean isMajor;
    private SSAFYInfo ssafyInfo;

    public AuthorElement(Member member, Boolean anonymity, Long number) {
        this(member, anonymity);
        if(anonymity) {
            this.nickname += number;
        }
    }
    public AuthorElement(Member member, Boolean anonymity) {
        this.memberId = anonymity? -1 : member.getId();
        this.nickname = anonymity? "익명" : member.getNickname();
        this.memberRole = member.getRole().getRoleType();
        this.ssafyMember = member.getSsafyMember();
        this.isMajor = member.getMajor();
        this.ssafyInfo = SSAFYInfo.from(member);
    }
}
