package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPostDetailResDto {
    private GetPostDetailElement post;

    public static GetPostDetailResDto of(Post post, Member loginMember) {

        return GetPostDetailResDto.builder()
                .post(GetPostDetailElement.of(post, loginMember))
                .build();
    }
}
