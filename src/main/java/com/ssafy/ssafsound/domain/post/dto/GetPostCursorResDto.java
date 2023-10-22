package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.post.domain.HotPost;
import com.ssafy.ssafsound.domain.post.domain.Post;
import com.ssafy.ssafsound.domain.post.domain.PostScrap;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetPostCursorResDto {
    private List<GetPostElement> posts;
    private Long cursor;

    public static GetPostCursorResDto ofPosts(List<Post> posts, int size) {
        Long nextCursor = null;
        int pageSize = posts.size() - 1;
        if (pageSize >= size) {
            posts = posts.subList(0, pageSize);
            nextCursor = posts.get(posts.size() - 1).getId();
        }

        return GetPostCursorResDto.builder()
                .posts(posts.stream()
                        .map(GetPostElement::new)
                        .collect(Collectors.toList()))
                .cursor(nextCursor)
                .build();

    }

    public static GetPostCursorResDto ofHotPosts(List<HotPost> hotPosts, int size) {
        Long nextCursor = null;
        int pageSize = hotPosts.size() - 1;
        if (pageSize >= size) {
            hotPosts = hotPosts.subList(0, pageSize);
            nextCursor = hotPosts.get(hotPosts.size() - 1).getId();
        }

        return GetPostCursorResDto.builder()
                .posts(hotPosts.stream()
                        .map(hotPost -> new GetPostElement(hotPost.getPost()))
                        .collect(Collectors.toList()))
                .cursor(nextCursor)
                .build();
    }

    public static GetPostCursorResDto ofPostScraps(List<PostScrap> postScraps, int size) {
        Long nextCursor = null;
        int pageSize = postScraps.size() - 1;
        if (pageSize >= size) {
            postScraps = postScraps.subList(0, pageSize);
            nextCursor = postScraps.get(postScraps.size() - 1).getId();
        }

        return GetPostCursorResDto.builder()
                .posts(postScraps.stream()
                        .map(postScrap -> new GetPostElement(postScrap.getPost()))
                        .collect(Collectors.toList()))
                .cursor(nextCursor)
                .build();
    }
}
