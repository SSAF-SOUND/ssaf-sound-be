package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.post.domain.HotPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotPostRepository extends JpaRepository<HotPost, Long>, HotPostCustomRepository {

    Optional<HotPost> findByPostId(Long postId);

    Boolean existsByPostId(Long postId);


    @EntityGraph(attributePaths = {"post", "post.board", "post.member", "post.likes"})
    @Query("select h from hot_post h")
    Page<HotPost> findHotPostsByPageable(PageRequest pageRequest);

    @EntityGraph(attributePaths = {"post", "post.board", "post.member", "post.likes"})
    @Query("select h from hot_post h " +
            "where replace(h.post.title, ' ', '') like CONCAT('%', :keyword, '%') " +
            "or replace(h.post.content, ' ', '') like CONCAT('%', :keyword, '%') ")
    Page<HotPost> searchHotPostsByKeywordAndPageable(String keyword, PageRequest pageRequest);
}
