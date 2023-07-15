package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberRole;
import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;
import com.ssafy.ssafsound.domain.post.domain.Post;
import com.ssafy.ssafsound.domain.post.domain.PostLike;
import com.ssafy.ssafsound.domain.post.domain.PostScrap;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetPostDetailElement {
    private final String title;
    private final String content;
    private final int likeCount;
    private final int commentCount;
    private final int scrapCount;
    private final LocalDateTime createdAt;
    private final String nickname;
    private final Boolean anonymous;
    private final Boolean modified;
    private final Boolean scraped;
    private final Boolean liked;
    private final List<ImageUrlElement> images;
    private final MemberRole memberRole;
    private final Boolean ssafyMember;
    private final Boolean isMajor;
    private final SSAFYInfo ssafyInfo;

    public static GetPostDetailElement of(Post post, Member member) {
        Boolean modified = post.getModifiedAt() != null;
        Boolean scraped = isScrap(post, member);
        Boolean liked = isLike(post, member);
        Boolean anonymous = post.getAnonymous();

        return GetPostDetailElement.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikes().size())
                .commentCount(post.getComments().size())
                .scrapCount(post.getScraps().size())
                .createdAt(post.getCreatedAt())
                .nickname(anonymous ? "익명" : post.getMember().getNickname())
                .anonymous(anonymous)
                .modified(modified)
                .scraped(scraped)
                .liked(liked)
                .images(findImageUrls(post))
                .memberRole(member.getRole())
                .ssafyMember(member.getSsafyMember())
                .isMajor(member.getMajor())
                .ssafyInfo(SSAFYInfo.from(member))
                .build();
    }

    private static Boolean isScrap(Post post, Member member) {
        List<PostScrap> scraps = post.getScraps();
        Long memberId = member.getId();

        for (PostScrap scrap : scraps) {
            if (scrap.getMember().getId().equals(memberId))
                return true;
        }
        return false;
    }

    private static Boolean isLike(Post post, Member member) {
        List<PostLike> likes = post.getLikes();
        Long memberId = member.getId();

        for (PostLike like : likes) {
            if (like.getMember().getId().equals(memberId))
                return true;
        }
        return false;
    }

    private static List<ImageUrlElement> findImageUrls(Post post) {
        return post.getImages().stream()
                .map(image -> ImageUrlElement.from(image.getImageUrl()))
                .collect(Collectors.toList());
    }
}
