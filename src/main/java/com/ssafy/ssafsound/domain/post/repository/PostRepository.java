package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository{
//    @EntityGraph(attributePaths = {"board", "member"})
//    List<Post> findWithDetailsByBoardId(@Param("boardId") Long boardId, Pageable pageable);

    boolean existsByIdAndMemberId(Long id, Long memberId);

    @Query("SELECT p FROM post p JOIN FETCH p.member WHERE p.id = :id")
    Optional<Post> findByIdWithMember(@Param("id") Long id);

    @Query("SELECT p FROM post p JOIN FETCH p.member LEFT JOIN FETCH p.images WHERE p.id = :id")
    Optional<Post> findWithMemberAndPostImageFetchById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"board", "member"})
    List<Post> findWithDetailsByMemberId(@Param("memberId") Long memberId, Pageable pageable);

//    @Query("SELECT p FROM post p " +
//            "JOIN FETCH p.board b " +
//            "JOIN FETCH p.member " +
//            "WHERE (REPLACE(p.title, ' ', '') LIKE CONCAT('%', :keyword, '%') " +
//            "OR REPLACE(p.content, ' ', '') LIKE CONCAT('%', :keyword, '%')) " +
//            "AND b.id = :boardId ")
//    List<Post> findWithDetailsFetchByBoardIdAndKeyword(@Param("boardId") Long boardId, @Param("keyword") String keyword, Pageable pageable);
}
