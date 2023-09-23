package com.ssafy.ssafsound.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@Builder
public class GetPostReqDto {
    private Long boardId;
    private Long cursor;

    @Min(value = 10, message = "Size가 너무 작습니다.")
    private int size;

}
