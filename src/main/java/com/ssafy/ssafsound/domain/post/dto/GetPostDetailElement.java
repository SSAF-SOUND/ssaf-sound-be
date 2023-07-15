package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
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
    private String title;
    private String content;
    private int likeCount;
    private int commentCount;
    private int scrapCount;
    private LocalDateTime createdAt;
    private Long memberId;
    private String nickname;
    private Boolean anonymous;
    private Boolean modified;
    private Boolean scraped;
    private Boolean liked;
    private List<ImageUrl> images;

    public static GetPostDetailElement from(Post post, AuthenticatedMember authenticatedMember) {
        boolean modified = post.getModifiedAt() != null;
        boolean scraped = isScrap(post, authenticatedMember);
        boolean liked = isLike(post, authenticatedMember);

        return GetPostDetailElement.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikes().size())
                .commentCount(post.getComments().size())
                .scrapCount(post.getScraps().size())
                .createdAt(post.getCreatedAt())
                .memberId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .anonymous(post.getAnonymous())
                .modified(modified)
                .scraped(scraped)
                .liked(liked)
                .images(findImageUrls(post))
                .build();
    }

    private static boolean isScrap(Post post, AuthenticatedMember authenticatedMember) {
        List<PostScrap> scraps = post.getScraps();
        for (PostScrap scrap : scraps) {
            if (scrap.getMember().getId().equals(authenticatedMember.getMemberId()))
                return true;
        }
        return false;
    }

    private static boolean isLike(Post post, AuthenticatedMember authenticatedMember) {
        List<PostLike> likes = post.getLikes();
        for (PostLike like : likes) {
            if (like.getMember().getId().equals(authenticatedMember.getMemberId()))
                return true;
        }
        return false;
    }

    private static List<ImageUrl> findImageUrls(Post post) {
        return post.getImages().stream()
                .map(i -> new ImageUrl(i.getImageUrl()))
                .collect(Collectors.toList());
    }
}
