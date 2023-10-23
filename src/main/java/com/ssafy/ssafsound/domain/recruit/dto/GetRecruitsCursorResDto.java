package com.ssafy.ssafsound.domain.recruit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetRecruitsCursorResDto implements AddParticipantDto {
    private List<RecruitElement> recruits;
    private Long nextCursor;
    private Boolean isLast;

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

    public static GetRecruitsCursorResDto fromPageAndMemberId(Slice<Recruit> sliceRecruit, Long memberId) {
        List<RecruitElement> recruits = sliceRecruit.toList()
                .stream()
                .map((recruit -> RecruitElement.fromRecruitAndLoginMemberId(recruit, memberId)))
                .collect(Collectors.toList());
        Long nextCursor = recruits.isEmpty() ? -1L : recruits.get(recruits.size()-1).getRecruitId();

        return GetRecruitsCursorResDto.builder()
                .recruits(recruits)
                .nextCursor(nextCursor)
                .isLast(sliceRecruit.isLast())
                .build();
    }
}
