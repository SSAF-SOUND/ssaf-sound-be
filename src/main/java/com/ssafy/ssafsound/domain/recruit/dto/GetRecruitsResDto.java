package com.ssafy.ssafsound.domain.recruit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetRecruitsResDto {
    private List<RecruitElement> recruits;
    private int currentPage;
    private int totalPages;
    private boolean isLastPage;

    @JsonIgnore
    public List<Long> getRecruitsId() {
        return recruits.stream().map(RecruitElement::getRecruitId).collect(Collectors.toList());
    }

    @JsonIgnore
    public Map<Long, Map<String, RecruitParticipant>> getRecruitParticipantMapByRecruitIdAndRecruitType() {
        Map<Long, Map<String, RecruitParticipant>> result = new TreeMap<>();
        for(RecruitElement recruitElement: recruits) {
            result.put(recruitElement.getRecruitId(), recruitElement.getRecruitParticipantMap());
        }
        return result;
    }

    public static GetRecruitsResDto fromPage(Page<Recruit> pageRecruits) {
        List<RecruitElement> recruits = pageRecruits.toList()
                .stream()
                .map(RecruitElement::from)
                .collect(Collectors.toList());

        return GetRecruitsResDto.builder()
                .recruits(recruits)
                .currentPage(pageRecruits.getNumber())
                .totalPages(pageRecruits.getTotalPages())
                .isLastPage(pageRecruits.isLast())
                .build();
    }
}
