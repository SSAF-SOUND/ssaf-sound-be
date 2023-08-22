package com.ssafy.ssafsound.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class PostCommentWriteReqDto {
    @Size(min = 2, max = 300)
    private String content;
    private Boolean anonymity;
}
