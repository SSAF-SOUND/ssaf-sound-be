package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.post.domain.Post;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = {"board", "member", "hotPost"})
    List<Post> findWithDetailsByBoardId(@Param("boardId") Long boardId, Pageable pageable);

    boolean existsByIdAndMemberId(Long id, Long memberId);
}
