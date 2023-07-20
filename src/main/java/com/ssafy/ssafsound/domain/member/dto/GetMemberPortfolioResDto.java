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

    private Boolean isPublic;

    private PortfolioElement portfolioElement;

    public static GetMemberPortfolioResDto of(Member member, MemberProfile memberProfile) {
        if(member.getPublicPortfolio()) {
            return GetMemberPortfolioResDto.builder()
                    .isPublic(true)
                    .portfolioElement(PortfolioElement.of(member, memberProfile))
                    .build();
        }
        return GetMemberPortfolioResDto.builder()
                .isPublic(false)
                .build();
    }
}
