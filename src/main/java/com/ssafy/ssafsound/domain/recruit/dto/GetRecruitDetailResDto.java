package com.ssafy.ssafsound.domain.recruit.dto;

import com.ssafy.ssafsound.domain.member.domain.AuthenticationStatus;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetRecruitDetailResDto {

    private long recruitId;
    private String title;
    private String content;
    private Long view;
    private boolean finishedRecruit;
    private String recruitStart;
    private String recruitEnd;
    private List<RecruitSkillElement> skills;
    private List<RecruitLimitElement> limits;
    private long memberId;
    private String nickName;
    private boolean ssafyMember;
    private boolean certification;
    private Integer year;

    public static GetRecruitDetailResDto from(Recruit recruit) {

        if(recruit.getDeletedRecruit()) throw new RecruitException(RecruitErrorInfo.IS_DELETED);

        Member register = recruit.getMember();
        List<RecruitSkillElement> skills = recruit.getSkills().stream()
                .map(RecruitSkillElement::from)
                .collect(Collectors.toList());
        List<RecruitLimitElement> limits = recruit.getLimitations().stream()
                .map(RecruitLimitElement::from)
                .collect(Collectors.toList());

        return GetRecruitDetailResDto.builder()
                .recruitId(recruit.getId())
                .title(recruit.getTitle())
                .content(recruit.getContent())
                .memberId(register.getId())
                .nickName(register.getNickname())
                .ssafyMember(register.getSsafyMember())
                .certification(register.getCertificationState().name().equals(AuthenticationStatus.CERTIFIED.name()))
                .year(register.getSemester())
                .recruitStart(recruit.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .recruitEnd(recruit.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .finishedRecruit(recruit.isFinishedRecruit())
                .skills(skills)
                .limits(limits)
                .view(recruit.getView())
                .build();
    }
}
