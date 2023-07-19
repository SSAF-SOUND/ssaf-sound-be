package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.post.domain.HotPost;

import java.util.List;

public interface HotPostCustomRepository {
    List<HotPost> findWithDetailsFetch(int cursor, int size);
}
