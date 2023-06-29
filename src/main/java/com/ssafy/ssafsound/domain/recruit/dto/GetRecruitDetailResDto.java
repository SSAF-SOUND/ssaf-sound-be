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

        String registerRecruitType = recruit.getRegisterRecruitType().getName();
        // 등록자의 모집 타입도 조회 api에서는 인원에 포함되어야한다.
        addRegisterRecruitType(limits, registerRecruitType);

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

    private static void addRegisterRecruitType(List<RecruitLimitElement> limits, String registerRecruitType) {
        for(RecruitLimitElement limit: limits) {
            if(limit.getRecruitType().equals(registerRecruitType)) {
                limit.addRegisterLimit();
                return;
            }
        }

        // 리크루트 등록 시 모집 인원 제한에 등록자가 포함되지 않는 경우 인원 제한 타입을 추가한다.
        limits.add(new RecruitLimitElement(registerRecruitType, 1));
    }
}
