package com.ssafy.ssafsound.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchCommentUpdateReqDto {
    @Size(min = 2, max = 300)
    private String content;
    private Boolean anonymity;
}
