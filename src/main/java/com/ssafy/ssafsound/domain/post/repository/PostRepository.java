package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    @Query("SELECT p FROM post p " +
//            "JOIN FETCH p.board " +
//            "JOIN FETCH p.member " +
//            "LEFT JOIN FETCH p.likes " +
//            "WHERE p.board.id = :boardId ")
//    List<Post> findByBoardIdWithDetailsFetch(@Param("boardId") Long boardId);
    @EntityGraph(attributePaths = {"board", "member", "likes", "hotPost"})
    List<Post> findWithDetailsByBoardId(@Param("boardId") Long boardId);

//    List<Post> findAllByBoardId(Long boardId, Pageable pageable);

    boolean existsByIdAndMemberId(Long id, Long memberId);
}
