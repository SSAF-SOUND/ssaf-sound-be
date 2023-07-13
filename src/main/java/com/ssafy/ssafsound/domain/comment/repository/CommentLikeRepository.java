package com.ssafy.ssafsound.domain.comment.repository;

import com.ssafy.ssafsound.domain.comment.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentIdAndMemberId(Long commentId, Long memberId);
    Long countById(Long id);
}
