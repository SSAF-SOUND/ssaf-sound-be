package com.ssafy.ssafsound.domain.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GetPostListResDto {

    private List<GetPostResDto> posts;

    public GetPostListResDto(List<GetPostResDto> posts) {
        this.posts = posts;
    }
}
