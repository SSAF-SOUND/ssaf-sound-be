package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.comment.dto.AuthorElement;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPostDetailResDto {
    private GetPostDetailElement post;
    private AuthorElement author;

    public static GetPostDetailResDto of(Post post, Member loginMember) {
        return GetPostDetailResDto.builder()
                .post(GetPostDetailElement.of(post, loginMember))
                .author(AuthorElement.from(post))
                .build();
    }
}
