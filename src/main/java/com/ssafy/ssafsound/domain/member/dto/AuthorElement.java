package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
        this.memberRole = anonymity? null : member.getRole().getRoleType();
        this.ssafyMember = anonymity? null : member.getSsafyMember();
        this.isMajor = anonymity? null : member.getMajor();
        this.ssafyInfo = (anonymity || !member.getSsafyMember())? null : SSAFYInfo.from(member);
    }
}
