package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class RecruitParticipantElement {
    private Integer limit;
    private List<ParticipantElement> members;

    public RecruitParticipantElement(Integer limit) {
        this.limit = limit;
        members = new ArrayList<>();
    }

    public void addMember(Long recruitApplicationId, LocalDateTime modifiedAt, Member member) {
        SSAFYInfo ssafyInfo = null;
        if(member.getSsafyMember()) {
            ssafyInfo = SSAFYInfo.from(member);
        }

        members.add(ParticipantElement.builder()
                .recruitApplicationId(recruitApplicationId)
                .joinedAt(modifiedAt)
                .memberId(member.getId())
                .nickname(member.getNickname())
                .isMajor(member.getMajor())
                .ssafyInfo(ssafyInfo)
                .build());
    }

    public void increaseLimit() {
        this.limit++;
    }
}
