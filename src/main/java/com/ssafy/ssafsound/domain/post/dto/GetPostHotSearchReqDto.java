package com.ssafy.ssafsound.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class GetPostHotSearchReqDto {
    @Size(min = 2)
    private String keyword;
}
