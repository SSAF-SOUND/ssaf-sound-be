package com.ssafy.ssafsound.domain.recruit.dto;

import com.ssafy.ssafsound.domain.member.domain.AuthenticationStatus;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class GetRecruitDetailResDto {
    private final RecruitDetail recruit;

    public static GetRecruitDetailResDto from(Recruit recruit) {

        if(recruit.getDeletedRecruit()) throw new RecruitException(RecruitErrorInfo.IS_DELETED);

        Member register = recruit.getMember();
        List<RecruitSkillElement> skills = recruit.getSkills().stream()
                .map(RecruitSkillElement::from)
                .collect(Collectors.toList());

        return new GetRecruitDetailResDto(RecruitDetail.builder()
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
                .view(recruit.getView())
                .build());
    }
}
