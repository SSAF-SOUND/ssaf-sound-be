package com.ssafy.ssafsound.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetHotPostResDto {
    private List<GetHotPost> posts;
}
