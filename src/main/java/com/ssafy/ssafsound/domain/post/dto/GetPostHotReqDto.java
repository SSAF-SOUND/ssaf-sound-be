package com.ssafy.ssafsound.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class GetPostHotReqDto {
    private int cursor;

    @Size(min = 10)
    private int size;
}
