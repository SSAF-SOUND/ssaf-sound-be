package com.ssafy.ssafsound.global.report.repository;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.global.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Boolean existsBySourceTypeAndSourceIdAndReportMemberId(MetaData sourceType, Long sourceId, Long reportMemberId);
}
