package com.ssafy.ssafsound.domain.recruit.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.dto.AuthorElement;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitQuestion;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetRecruitDetailResDto {

    private String category;
    private long recruitId;
    private String title;
    private String content;
    private String contactURI;
    private Long view;
    private boolean finishedRecruit;
    private String recruitStart;
    private String recruitEnd;
    private List<RecruitSkillElement> skills;
    private List<GetRecruitLimitDetail> limits;
    private List<String> questions;

    private AuthorElement author;
    private long scrapCount;
    private Boolean scraped;
    private String matchStatus;
    private Boolean mine;

    public static GetRecruitDetailResDto of(Recruit recruit, long scrapCount, Boolean scraped, Boolean mine, RecruitApplication recruitApplication) {

        if(recruit.getDeletedRecruit()) throw new RecruitException(RecruitErrorInfo.IS_DELETED);

        Member register = recruit.getMember();
        List<RecruitSkillElement> skills = recruit.getSkills().stream()
                .map(RecruitSkillElement::from)
                .collect(Collectors.toList());

        List<GetRecruitLimitDetail> limits = recruit.getLimitations().stream()
                .map(GetRecruitLimitDetail::from)
                .collect(Collectors.toList());

        String registerRecruitType = recruit.getRegisterRecruitType().getName();
        // 등록자의 모집 타입도 조회 api에서는 인원에 포함되어야한다.
        addRegisterRecruitType(limits, registerRecruitType);
        List<String> questions = recruit.getQuestions().stream().map(RecruitQuestion::getContent).collect(Collectors.toList());

        String matchStatus = (recruitApplication != null) ? recruitApplication.getMatchStatus().name() : MatchStatus.INITIAL.name();
        if(matchStatus.equals(MatchStatus.CANCEL.name())) {
            matchStatus = MatchStatus.INITIAL.name();
        }
        return GetRecruitDetailResDto.builder()
                .category(recruit.getCategory().name())
                .recruitId(recruit.getId())
                .title(recruit.getTitle())
                .content(recruit.getContent())
                .contactURI(recruit.getContactURI())
                .recruitStart(recruit.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .recruitEnd(recruit.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .finishedRecruit(recruit.getFinishedRecruit())
                .skills(skills)
                .limits(limits)
                .questions(questions)
                .view(recruit.getView())
                .author(new AuthorElement(register, false))
                .scrapCount(scrapCount)
                .scraped(scraped)
                .matchStatus(matchStatus)
                .mine(mine)
                .build();
    }

    private static void addRegisterRecruitType(List<GetRecruitLimitDetail> limits, String registerRecruitType) {
        for(GetRecruitLimitDetail limit: limits) {
            if(limit.getRecruitType().equals(registerRecruitType)) {
                limit.addRegisterLimit();
                return;
            }
        }

        // 리크루트 등록 시 모집 인원 제한에 등록자가 포함되지 않는 경우 인원 제한 타입을 추가한다.
        limits.add(new GetRecruitLimitDetail(registerRecruitType, 1, 1));
    }
}
