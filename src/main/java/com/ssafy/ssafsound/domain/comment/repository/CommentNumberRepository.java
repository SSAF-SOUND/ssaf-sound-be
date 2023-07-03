package com.ssafy.ssafsound.domain.comment.repository;

import com.ssafy.ssafsound.domain.comment.domain.CommentNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentNumberRepository extends JpaRepository<CommentNumber, Long> {
    long countAllByPostId(Long postId);

    Optional<CommentNumber> findByPostIdAndMemberId(Long postId, Long memberId);
}
