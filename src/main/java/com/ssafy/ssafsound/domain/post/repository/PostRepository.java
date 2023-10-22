package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.board.domain.Board;
import com.ssafy.ssafsound.domain.post.domain.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
    boolean existsByIdAndMemberId(Long id, Long memberId);

    @Query("SELECT p FROM post p JOIN FETCH p.member WHERE p.id = :id")
    Optional<Post> findByIdWithMember(@Param("id") Long id);

    @Query("SELECT p FROM post p " +
            "JOIN FETCH p.member " +
            "JOIN FETCH p.board " +
            "LEFT JOIN FETCH p.images " +
            "WHERE p.id = :id")
    Optional<Post> findWithMemberAndPostImageFetchById(@Param("id") Long id);

    @Query(value = "SELECT * FROM post WHERE post_id = :id", nativeQuery = true)
    Optional<Post> findByIdRegardlessOfDeleted(@Param("id") Long id);

    @Query("select p from post p " +
            "join fetch p.board b " +
            "join fetch p.member " +
            "left join fetch p.likes " +
            "where b = :board ")
    List<Post> findPostsByboardAndPageable(@Param("board") Board board, Pageable pageable);

    @Query("select p from post p " +
            "join fetch p.board b " +
            "join fetch p.member " +
            "left join fetch p.likes " +
            "where (replace(p.title, ' ', '') like CONCAT('%', :keyword, '%') " +
            "or replace(p.content, ' ', '') like CONCAT('%', :keyword, '%')) " +
            "and b = :board ")
    List<Post> searchPostsByBoardAndKeywordAndPageable(Board board, String keyword, PageRequest pageRequest);
}
