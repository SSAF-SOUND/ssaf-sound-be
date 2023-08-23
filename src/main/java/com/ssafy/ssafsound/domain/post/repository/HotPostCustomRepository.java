package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.post.domain.HotPost;

import java.util.List;

public interface HotPostCustomRepository {
    List<HotPost> findHotPosts(Long cursor, int size);

    List<HotPost> findHotPostsByKeyword(String keyword, Long cursor, int size);
}
