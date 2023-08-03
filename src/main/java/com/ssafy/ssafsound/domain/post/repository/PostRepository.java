package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
