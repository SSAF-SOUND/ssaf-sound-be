package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.board.domain.Board;
import com.ssafy.ssafsound.domain.post.domain.Post;
import com.ssafy.ssafsound.domain.post.domain.PostImage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class GetPostElement {
    private Long boardId;
    private String boardTitle;
    private Long postId;
    private String title;
    private String content;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private String nickname;
    private Boolean anonymous;
    private String thumbnail;

    public static GetPostElement from(Post post) {
        String thumbnail = findThumbnailUrl(post);
        Boolean anonymous = post.getAnonymous();
        Board board = post.getBoard();

        return GetPostElement.builder()
                .boardId(board.getId())
                .boardTitle(board.getTitle())
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikes().size())
                .commentCount(post.getComments().size())
                .createdAt(post.getCreatedAt())
                .nickname(anonymous ? "익명" : post.getMember().getNickname())
                .anonymous(anonymous)
                .thumbnail(thumbnail)
                .build();
    }

    private static String findThumbnailUrl(Post post) {
        List<PostImage> images = post.getImages();
        if (images.size() >= 1)
            return images.get(0).getImageUrl();
        return null;
    }
}