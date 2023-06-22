package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.post.domain.HotPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotPostRepository extends JpaRepository<HotPost, Long> {
    @Modifying
    @Query("DELETE FROM hot_post h " +
            "WHERE h.id IN ( " +
            "  SELECT h.id " +
            "  FROM hot_post h LEFT JOIN post_like p " +
            "  ON h.post.id = p.post.id " +
            "  GROUP BY h.id " +
            "  HAVING COUNT(p.id) < :threshold " +
            ")")
    void deleteAllWithDecreasedLikes(@Param("threshold") Long threshold);
}
