package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecruitParticipantElement {
    private String recruitType;
    private Long memberId;
    private Integer year;
    private String nickName;
    private String certificationState;
    private Boolean ssafyMember;
    private Boolean major;
    private String majorType;

    public static RecruitParticipantElement from(RecruitApplication recruitApplication) {
        Member member = recruitApplication.getMember();
        String majorType = null;
        if(member.getMajorType() != null) {
            majorType = member.getMajorType().getName();
        }

        return RecruitParticipantElement.builder()
                .recruitType(recruitApplication.getType().getName())
                .memberId(member.getId())
                .year(member.getSemester())
                .nickName(member.getNickname())
                .certificationState(member.getCertificationState().name())
                .ssafyMember(member.getSsafyMember())
                .major(member.getMajor())
                .majorType(majorType)
                .build();
    }
}
