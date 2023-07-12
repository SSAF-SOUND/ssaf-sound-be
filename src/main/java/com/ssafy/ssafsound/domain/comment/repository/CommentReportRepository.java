package com.ssafy.ssafsound.domain.comment.repository;

import com.ssafy.ssafsound.domain.comment.domain.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
    boolean existsByCommentIdAndMemberId(Long commentId, Long memberId);
}
