package com.ssafy.ssafsound.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetPostListResDto {
    private List<GetPostResDto> posts;
}
