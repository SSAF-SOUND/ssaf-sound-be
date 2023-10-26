package com.ssafy.ssafsound.domain.recruit.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Builder
public class GetRecruitsCursorReqDto implements RecruitPaging<Long>{
    private Long cursor;
    private Integer size;

    @NotBlank
    private String category;
    private String keyword;
    private boolean isFinished;

    @NotEmpty
    private List<String> recruitTypes;
    private List<String> skills;

    @Override
    public Long getNextPaging() {
        return this.cursor;
    }
}
