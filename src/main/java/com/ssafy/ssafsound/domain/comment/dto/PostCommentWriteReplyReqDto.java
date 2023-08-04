package com.ssafy.ssafsound.domain.comment.dto;

import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
public class PostCommentWriteReplyReqDto {
    @Size(min = 2)
    private String content;
    private Boolean anonymity;
}
