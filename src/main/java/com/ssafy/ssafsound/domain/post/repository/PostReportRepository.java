package com.ssafy.ssafsound.domain.post.repository;

import com.ssafy.ssafsound.domain.post.domain.PostReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    boolean existsByPostIdAndMemberId(Long postId, Long memberId);
}
