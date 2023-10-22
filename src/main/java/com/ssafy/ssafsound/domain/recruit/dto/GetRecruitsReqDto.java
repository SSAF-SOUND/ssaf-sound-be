package com.ssafy.ssafsound.domain.recruit.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Builder
public class GetRecruitsReqDto {
    private Long next;

    @NotBlank
    private String category;
    private String keyword;
    private boolean isFinished;

    @NotEmpty
    private List<String> recruitTypes;
    private List<String> skills;
}
