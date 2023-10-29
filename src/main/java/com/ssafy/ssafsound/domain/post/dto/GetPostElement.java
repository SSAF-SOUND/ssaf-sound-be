package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.board.domain.Board;
import com.ssafy.ssafsound.domain.post.domain.Post;
import com.ssafy.ssafsound.domain.post.domain.PostImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Boolean anonymity;
    private String thumbnail;

    public GetPostElement(Post post) {
        Boolean anonymity = post.getAnonymity();
        Board board = post.getBoard();

        this.boardId = board.getId();
        this.boardTitle = board.getTitle();
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.likeCount = post.getLikes().size();
        this.commentCount = post.getCommentCount();
        this.createdAt = post.getCreatedAt();
        this.nickname = anonymity ? "익명" : post.getMember().getNickname();
        this.anonymity = anonymity;
        this.thumbnail = findThumbnailUrl(post);
    }

    private String findThumbnailUrl(Post post) {
        List<PostImage> images = post.getImages();
        if (images.size() >= 1)
            return images.get(0).getImageUrl();
        return null;
    }
}