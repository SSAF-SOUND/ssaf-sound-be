package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.post.validator.CheckCursor;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPostReqDto {
    private Long boardId;
    private Long cursor = -1L;

    @Min(value = 10, message = "Size가 너무 작습니다.")
    private int size;
}
