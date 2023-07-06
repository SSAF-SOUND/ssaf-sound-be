package com.ssafy.ssafsound.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class PutCommentUpdateReqDto {
    @Size(min = 2)
    private String content;
    private Boolean anonymous;
}
