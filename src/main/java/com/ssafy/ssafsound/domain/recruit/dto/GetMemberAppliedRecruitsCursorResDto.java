package com.ssafy.ssafsound.domain.recruit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetMemberAppliedRecruitsCursorResDto implements AddParticipantDto {
    private List<AppliedRecruitElement> recruits;
    private Long nextCursor;
    private Boolean isLast;

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

    public static GetMemberAppliedRecruitsCursorResDto fromPageAndMemberId(Slice<AppliedRecruit> appliedRecruitSlice, Long memberId) {
        List<AppliedRecruitElement> recruits = appliedRecruitSlice.toList()
                .stream()
                .map((appliedRecruit -> AppliedRecruitElement.fromRecruitAndLoginMemberId(appliedRecruit, memberId)))
                .collect(Collectors.toList());
        Long nextCursor = recruits.isEmpty() ? -1L : recruits.get(recruits.size()-1).getRecruitId();

        return GetMemberAppliedRecruitsCursorResDto.builder()
                .recruits(recruits)
                .nextCursor(nextCursor)
                .isLast(appliedRecruitSlice.isLast())
                .build();
    }
}
