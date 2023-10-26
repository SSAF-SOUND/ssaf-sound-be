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
public class GetRecruitsPageResDto implements AddParticipantDto {
    private List<RecruitElement> recruits;
    private Integer currentPage;
    private Integer totalPageCount;

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

    public static GetRecruitsPageResDto fromPageAndMemberId(Page<Recruit> pageRecruit, Long memberId) {
        List<RecruitElement> recruits = pageRecruit.getContent()
                .stream()
                .map((recruit -> RecruitElement.fromRecruitAndLoginMemberId(recruit, memberId)))
                .collect(Collectors.toList());

        return GetRecruitsPageResDto.builder()
                .recruits(recruits)
                .currentPage(pageRecruit.getNumber()+1)
                .totalPageCount(pageRecruit.getTotalPages())
                .build();
    }
}
