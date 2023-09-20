package com.ssafy.ssafsound.domain.recruit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class RecruitElement {
    private Long recruitId;
    private String category;
    private String title;
    private boolean finishedRecruit;
    private String recruitEnd;
    private String content;
    private List<RecruitSkillElement> skills;
    private List<RecruitParticipant> participants;

    @JsonIgnore
    public Map<String, RecruitParticipant> getRecruitParticipantMap() {
        Map<String, RecruitParticipant> recruitTypeMaps = new HashMap<>();
        for(RecruitParticipant participant: participants) {
            recruitTypeMaps.put(participant.getRecruitType(), participant);
        }

        return recruitTypeMaps;
    }

    public static RecruitElement from(Recruit recruit) {
        List<RecruitSkillElement> skills = recruit.getSkills()
                .stream()
                .map(RecruitSkillElement::from).collect(Collectors.toList());

        List<RecruitParticipant> recruitParticipants = recruit.getLimitations()
                .stream()
                .map(RecruitParticipant::from).collect(Collectors.toList());
        addRegisterRecruitParticipant(recruit, recruitParticipants);

        // 컨텐츠의 길이가 너무 긴 경우, 일부만 보내준다.
        String content = recruit.getContent();
        if(content.length() > 20) content = content.substring(0, 20);

        return RecruitElement.builder()
                .recruitId(recruit.getId())
                .category(recruit.getCategory().name())
                .title(recruit.getTitle())
                .finishedRecruit(recruit.getFinishedRecruit())
                .recruitEnd(recruit.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .content(content)
                .participants(recruitParticipants)
                .skills(skills)
                .build();
    }

    private static void addRegisterRecruitParticipant(Recruit recruit, List<RecruitParticipant> recruitParticipants) {
        // 등록자가 선택한 모집군이 등록 모집 인원에 포함되어있는 경우, 해당 모집군에 등록자를 포함시키고, 아닌 경우 등록자의 모집군 정보를 추가한다.
        Member register = recruit.getMember();
        String registerRecruitType = recruit.getRegisterRecruitType().getName();

        for(RecruitParticipant recruitParticipant: recruitParticipants) {
            if(recruitParticipant.getRecruitType().equals(registerRecruitType)) {
                recruitParticipant.addRegister(register.getNickname(), register.getMajor());
                return;
            }
        }

        // 리크루트 등록 시 모집 인원 제한에 등록자가 포함되지 않는 경우 인원 제한 타입을 추가한다.
        recruitParticipants.add(RecruitParticipant.builder()
                .recruitType(registerRecruitType)
                .limit(1)
                .members(List.of(Participant.builder()
                        .nickname(register.getNickname())
                        .isMajor(register.getMajor())
                        .build()))
                .build());
    }
}
