package com.ssafy.ssafsound.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@Builder
public class GetPostOffsetReqDto {
    private Long boardId;
    private Integer page;

    @Min(value = 10, message = "Size가 너무 작습니다.")
    private Integer size;

    public void setPage(Integer page) {
        if (page <= 0 || page == null){
            this.page = 1;
            return;
        }
        this.page = page;
    }
}
