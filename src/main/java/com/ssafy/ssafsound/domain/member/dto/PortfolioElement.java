package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PortfolioElement {

    private String selfIntroduction;

    private List<String> skills;

    private List<PutMemberLink> memberLinks;

    public static PortfolioElement of(Member member, MemberProfile memberProfile) {
        return PortfolioElement.builder()
                .selfIntroduction(memberProfile.getIntroduce())
                .skills(member.getMemberSkills()
                        .stream()
                        .map(memberSkill -> memberSkill.getSkill().getName())
                        .collect(Collectors.toList()))
                .memberLinks(member.getMemberLinks()
                        .stream()
                        .map(memberLink -> new PutMemberLink(memberLink.getLinkName(), memberLink.getPath()))
                        .collect(Collectors.toList()))
                .build();
    }
}
