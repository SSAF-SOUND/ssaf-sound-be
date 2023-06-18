package com.ssafy.ssafsound.domain.recruitcomment.repository;

import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruitCommentLikeRepository extends JpaRepository<RecruitCommentLike, Long> {
    Optional<RecruitCommentLike> findByRecruitCommentIdAndMemberId(Long recruitCommentId, Long memberId);
}
