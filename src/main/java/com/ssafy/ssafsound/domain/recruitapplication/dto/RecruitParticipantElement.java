package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecruitParticipantElement {
    private String recruitType;
    private Long memberId;
    private String nickName;
    private Boolean isMajor;
    private SSAFYInfo ssafyInfo;

    public static RecruitParticipantElement from(RecruitApplication recruitApplication) {
        Member member = recruitApplication.getMember();
        SSAFYInfo ssafyInfo = null;
        if(member.getSsafyMember()) {
            ssafyInfo = SSAFYInfo.from(member);
        }

        return RecruitParticipantElement.builder()
                .recruitType(recruitApplication.getType().getName())
                .memberId(member.getId())
                .nickName(member.getNickname())
                .isMajor(member.getMajor())
                .ssafyInfo(ssafyInfo)
                .build();
    }

    public static RecruitParticipantElement from(Recruit recruit) {
        Member register = recruit.getMember();
        SSAFYInfo ssafyInfo = null;
        if(register.getSsafyMember()) {
            ssafyInfo = SSAFYInfo.from(register);
        }

        return RecruitParticipantElement.builder()
                .recruitType(recruit.getRegisterRecruitType().getName())
                .memberId(register.getId())
                .nickName(register.getNickname())
                .isMajor(register.getMajor())
                .ssafyInfo(ssafyInfo)
                .build();
    }
}
