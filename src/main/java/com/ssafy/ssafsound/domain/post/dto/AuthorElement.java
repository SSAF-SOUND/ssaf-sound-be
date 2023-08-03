package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;
import com.ssafy.ssafsound.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthorElement {
    private final String nickname;
    private final String memberRole;
    private final Boolean ssafyMember;
    private final Boolean isMajor;
    private final SSAFYInfo ssafyInfo;

    public static AuthorElement from(Post post) {
        Member member = post.getMember();
        Boolean anonymous = post.getAnonymous();

        return AuthorElement.builder()
                .nickname(anonymous ? "익명" : post.getMember().getNickname())
                .memberRole(member.getRole().getRoleType())
                .ssafyMember(member.getSsafyMember())
                .isMajor(member.getMajor())
                .ssafyInfo(SSAFYInfo.from(member))
                .build();
    }
}
