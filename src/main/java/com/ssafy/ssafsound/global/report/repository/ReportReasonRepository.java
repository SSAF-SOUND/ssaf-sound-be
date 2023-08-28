package com.ssafy.ssafsound.global.report.repository;

import com.ssafy.ssafsound.global.report.domain.ReportReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportReasonRepository extends JpaRepository<ReportReason, Long> {
}
