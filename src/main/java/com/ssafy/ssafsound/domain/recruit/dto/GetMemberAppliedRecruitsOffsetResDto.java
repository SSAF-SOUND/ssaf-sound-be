package com.ssafy.ssafsound.domain.recruit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetMemberAppliedRecruitsOffsetResDto implements AddParticipantDto {
    private List<AppliedRecruitElement> recruits;
    private int currentPage;
    private int totalPageCount;

    @JsonIgnore
    public List<Long> getRecruitsId() {
        return recruits.stream().map(AppliedRecruitElement::getRecruitId).collect(Collectors.toList());
    }

    @JsonIgnore
    public Map<Long, Map<String, RecruitParticipant>> getRecruitParticipantMapByRecruitIdAndRecruitType() {
        Map<Long, Map<String, RecruitParticipant>> result = new TreeMap<>();
        for(AppliedRecruitElement recruitElement: recruits) {
            result.put(recruitElement.getRecruitId(), recruitElement.getRecruitParticipantMap());
        }
        return result;
    }

    public static GetMemberAppliedRecruitsOffsetResDto fromPageAndMemberId(Page<AppliedRecruit> appliedRecruitPage, Long memberId) {
        List<AppliedRecruitElement> recruits = appliedRecruitPage.getContent()
                .stream()
                .map((appliedRecruit -> AppliedRecruitElement.fromRecruitAndLoginMemberId(appliedRecruit, memberId)))
                .collect(Collectors.toList());

        return GetMemberAppliedRecruitsOffsetResDto.builder()
                .recruits(recruits)
                .currentPage(appliedRecruitPage.getNumber()+1)
                .totalPageCount(appliedRecruitPage.getTotalPages())
                .build();
    }
}
