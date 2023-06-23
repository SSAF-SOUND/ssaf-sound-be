package com.ssafy.ssafsound.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostPostWriteReqDto {
    private String title;
    private String content;
    private boolean anonymous;
}
