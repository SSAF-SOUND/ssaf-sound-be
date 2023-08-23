package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.post.domain.Post;
import com.ssafy.ssafsound.domain.post.domain.PostScrap;

import java.util.List;

public interface PostScrapCustomRepository {
    List<PostScrap> findMyScrapPosts(Long memberId, Long cursor, int size);
}
