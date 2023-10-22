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
public class GetRecruitOffsetResDto implements AddParticipantDto {
    private List<RecruitElement> recruits;
    private int currentPage;
    private int totalPageCount;
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

    public static GetRecruitOffsetResDto fromPageAndMemberId(Page<Recruit> recruitPages, Long memberId) {
        List<RecruitElement> recruits = recruitPages.getContent()
                .stream()
                .map((recruit -> RecruitElement.fromRecruitAndLoginMemberId(recruit, memberId)))
                .collect(Collectors.toList());

        return GetRecruitOffsetResDto.builder()
                .recruits(recruits)
                .currentPage(recruitPages.getNumber())
                .totalPageCount(recruitPages.getTotalPages())
                .build();
    }
}
