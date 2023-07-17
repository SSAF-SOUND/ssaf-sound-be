package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberProfile;
import com.ssafy.ssafsound.domain.meta.validator.CheckSkills;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PutMemberProfileReqDto {
    private String selfIntroduction;

    @CheckSkills
    private List<String> skills;

    private List<PutMemberLink> memberLinks;

    public MemberProfile toMemberProfile(Member member) {
        return MemberProfile.builder()
                .member(member)
                .introduce(this.selfIntroduction)
                .build();
    }
}
