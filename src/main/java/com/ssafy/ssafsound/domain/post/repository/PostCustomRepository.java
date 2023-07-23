package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.post.domain.Post;

import java.util.List;

public interface PostCustomRepository {
    List<Post> findWithDetailsByBoardId(Long boardId, Long cursor, int size);
}
