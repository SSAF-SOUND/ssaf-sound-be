package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.post.domain.Post;
import com.ssafy.ssafsound.domain.post.domain.PostImage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class GetPost {
    private String boardTitle;
    private String title;
    private String content;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createAt;
    private Long memberId;
    private String nickname;
    private Boolean anonymous;
    private String thumbnail;

    public static GetPost from(Post post) {
        String thumbnail = findThumbnailUrl(post);

        return GetPost.builder()
                .boardTitle(post.getBoard().getTitle())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikes().size())
                .commentCount(post.getComments().size())
                .createAt(post.getCreatedAt())
                .memberId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .anonymous(post.getAnonymous())
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