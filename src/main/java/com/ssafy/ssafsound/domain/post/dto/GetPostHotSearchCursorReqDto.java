package com.ssafy.ssafsound.domain.post.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPostHotSearchCursorReqDto {
    @Size(min = 2)
    @NotBlank
    private String keyword;

    @Builder.Default
    private Long cursor = -1L;

    @Min(value = 10, message = "Size가 너무 작습니다.")
    @Builder.Default
    private int size = 10;
}
