package com.ssafy.ssafsound.domain.recruit.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RecruitDetail {
    private long recruitId;
    private String title;
    private String content;
    private Long view;
    private boolean finishedRecruit;
    private String recruitStart;
    private String recruitEnd;
    private List<RecruitSkillElement> skills;
    private long memberId;
    private String nickName;
    private boolean ssafyMember;
    private boolean certification;
    private Integer year;
}
