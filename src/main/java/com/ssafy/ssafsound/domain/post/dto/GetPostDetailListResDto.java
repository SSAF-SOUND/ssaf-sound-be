package com.ssafy.ssafsound.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostDetailListResDto {
    private List<GetPostDetailResDto> post;

}
