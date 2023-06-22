package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRecruitApplicationReqDto {

    private String recruitType;
    private List<String> contents;

    public RecruitApplication createRecruitApplicationFromPredefinedMetadata(Member member, Recruit recruit, MetaData recruitType) {
        return RecruitApplication.builder()
                .member(member)
                .recruit(recruit)
                .type(recruitType)
                .matchStatus(MatchStatus.WAITING_REGISTER_APPROVE)
                .build();
    }
}
