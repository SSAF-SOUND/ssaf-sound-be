package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetMemberPortfolioResDto {

    private PortfolioElement portfolioElement;

    public static GetMemberPortfolioResDto ofMemberProfile(Member member, MemberProfile memberProfile) {
        return GetMemberPortfolioResDto.builder()
                .portfolioElement(PortfolioElement.of(member, memberProfile))
                .build();
    }
}
