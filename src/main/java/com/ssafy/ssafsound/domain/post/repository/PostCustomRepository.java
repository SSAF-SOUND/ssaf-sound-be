package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.post.domain.Post;

import java.util.List;

public interface PostCustomRepository {
    List<Post> findWithDetailsByBoardId(Long boardId, Long cursor, int size);

    List<Post> findWithDetailsFetchByBoardIdAndKeyword(Long boardId, String keyword, Long cursor, int size);
}
