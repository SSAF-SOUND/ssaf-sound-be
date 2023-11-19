package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.board.domain.Board;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

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

    @EntityGraph(attributePaths = {"board", "member", "likes"})
    @Query(value = "select p from post p " +
            "where p.board = :board ")
    Page<Post> findPostsByBoardAndPageable(@Param("board") Board board, Pageable pageable);

    @EntityGraph(attributePaths = {"board", "member", "likes"})
    @Query("select p from post p " +
            "where (replace(p.title, ' ', '') like CONCAT('%', :keyword, '%') " +
            "or replace(p.content, ' ', '') like CONCAT('%', :keyword, '%')) ")
    Page<Post> searchAllPostsByBoardAndKeywordAndPageable(String keyword, PageRequest pageRequest);

    @EntityGraph(attributePaths = {"board", "member", "likes"})
    @Query("select p from post p " +
            "where (replace(p.title, ' ', '') like CONCAT('%', :keyword, '%') " +
            "or replace(p.content, ' ', '') like CONCAT('%', :keyword, '%')) " +
            "and p.board = :board ")
    Page<Post> searchPostsByBoardAndKeywordAndPageable(Board board, String keyword, PageRequest pageRequest);

    @EntityGraph(attributePaths = {"board", "member", "likes"})
    @Query("select p from post p " +
            "where p.member = :member ")
    Page<Post> findMyPostsByMemberAndPageable(@Param("member") Member member, PageRequest pageRequest);

    @EntityGraph(attributePaths = {"board", "member", "likes"})
    @Query(value = "select p from post p ")
    Page<Post> findAllWithPageable(Pageable pageable);
}
