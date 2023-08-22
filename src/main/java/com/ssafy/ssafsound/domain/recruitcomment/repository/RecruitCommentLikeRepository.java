package com.ssafy.ssafsound.domain.recruitcomment.repository;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecruitCommentLikeRepository extends JpaRepository<RecruitCommentLike, Long> {
    Optional<RecruitCommentLike> findByRecruitCommentIdAndMemberId(Long recruitCommentId, Long memberId);

    int countById(Long recruitCommentLikeId);

    @Query("SELECT rcl FROM recruit_comment_like rcl inner join fetch rcl.recruitComment where rcl.recruitComment.recruit = :recruit and rcl.member.id = :memberId")
    List<RecruitCommentLike> findByRecruitCommentRecruitAndMemberIdFetchRecruitComment(Recruit recruit, Long memberId);
}
