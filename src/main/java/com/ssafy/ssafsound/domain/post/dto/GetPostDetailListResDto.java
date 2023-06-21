package com.ssafy.ssafsound.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetPostDetailListResDto {
    private List<GetPostDetailResDto> post;

}
