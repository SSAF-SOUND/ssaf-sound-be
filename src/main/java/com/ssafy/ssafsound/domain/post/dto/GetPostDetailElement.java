package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.board.domain.Board;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.dto.AuthorElement;
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
    private final Long boardId;
    private final String boardTitle;
    private final Long postId;
    private final String title;
    private final String content;
    private final int likeCount;
    private final int commentCount;
    private final int scrapCount;
    private final LocalDateTime createdAt;
    private final Boolean anonymity;
    private final Boolean modified;
    private final Boolean scraped;
    private final Boolean liked;
    private final Boolean mine;
    private final List<ImageUrlElement> images;
    private final AuthorElement author;

    public static GetPostDetailElement of(Post post, Member loginMember) {
        Boolean scraped = isScrap(post, loginMember);
        Boolean liked = isLike(post, loginMember);
        Boolean mine = isMine(post, loginMember);
        List<ImageUrlElement> images = findImageUrls(post);
        Board board = post.getBoard();

        return GetPostDetailElement.builder()
                .boardId(board.getId())
                .boardTitle(board.getTitle())
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikes().size())
                .commentCount(post.getCommentCount())
                .scrapCount(post.getScraps().size())
                .createdAt(post.getCreatedAt())
                .anonymity(post.getAnonymity())
                .modified(post.getModifiedAt() != null)
                .scraped(scraped)
                .liked(liked)
                .mine(mine)
                .images(images)
                .author(new AuthorElement(post.getMember(), post.getAnonymity()))
                .build();
    }

    private static Boolean isScrap(Post post, Member loginMember) {
        if (loginMember == null)
            return false;

        List<PostScrap> scraps = post.getScraps();
        Long loginMemberId = loginMember.getId();

        for (PostScrap scrap : scraps) {
            if (scrap.getMember().getId().equals(loginMemberId))
                return true;
        }
        return false;
    }

    private static Boolean isLike(Post post, Member loginMember) {
        if (loginMember == null)
            return false;

        List<PostLike> likes = post.getLikes();
        Long loginMemberId = loginMember.getId();

        for (PostLike like : likes) {
            if (like.getMember().getId().equals(loginMemberId))
                return true;
        }
        return false;
    }

    private static List<ImageUrlElement> findImageUrls(Post post) {
        return post.getImages().stream()
                .map(image -> ImageUrlElement.from(image.getImageUrl()))
                .collect(Collectors.toList());
    }

    private static Boolean isMine(Post post, Member loginMember) {
        if (loginMember == null)
            return false;

        return post.getMember().getId().equals(loginMember.getId());
    }
}
