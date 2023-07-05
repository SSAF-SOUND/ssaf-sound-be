package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
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

    public static RecruitParticipantElement from(Recruit recruit) {
        Member register = recruit.getMember();
        String majorType = null;
        if(register.getMajorType() != null) {
            majorType = register.getMajorType().getName();
        }

        return RecruitParticipantElement.builder()
                .recruitType(recruit.getRegisterRecruitType().getName())
                .memberId(register.getId())
                .year(register.getSemester())
                .nickName(register.getNickname())
                .certificationState(register.getCertificationState().name())
                .ssafyMember(register.getSsafyMember())
                .major(register.getMajor())
                .majorType(majorType)
                .build();
    }
}
