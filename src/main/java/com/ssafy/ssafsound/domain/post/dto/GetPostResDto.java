package com.ssafy.ssafsound.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetPostResDto {
    private List<GetPost> posts;
}
