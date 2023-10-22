package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.post.domain.HotPost;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotPostRepository extends JpaRepository<HotPost, Long>, HotPostCustomRepository {
    @Modifying
    @Query("DELETE FROM hot_post h " +
            "WHERE h.id IN ( " +
            "  SELECT h.id " +
            "  FROM hot_post h LEFT JOIN post_like p " +
            "  ON h.post.id = p.post.id " +
            "  GROUP BY h.id " +
            "  HAVING COUNT(p.id) < :threshold " +
            ")")
    void deleteHotPostsUnderThreshold(@Param("threshold") Long threshold);

    Optional<HotPost> findByPostId(Long postId);

    Boolean existsByPostId(Long postId);

    @Query("select h from hot_post h " +
            "join fetch h.post p " +
            "join fetch p.board " +
            "join fetch p.member " +
            "left join fetch p.likes ")
    List<HotPost> findHotPostsByPageable(PageRequest pageRequest);

    @Query("select h from hot_post h " +
            "join fetch h.post p " +
            "join fetch p.board b " +
            "join fetch p.member " +
            "left join fetch p.likes " +
            "where replace(p.title, ' ', '') like CONCAT('%', :keyword, '%') " +
            "or replace(p.content, ' ', '') like CONCAT('%', :keyword, '%') ")
    List<HotPost> searchHotPostsByKeywordAndPageable(String keyword, PageRequest pageRequest);
}
