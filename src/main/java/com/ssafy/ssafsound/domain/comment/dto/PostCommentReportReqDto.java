package com.ssafy.ssafsound.domain.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class PostCommentReportReqDto {
    @Size(min = 2)
    private String content;
}