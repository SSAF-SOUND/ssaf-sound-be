package com.ssafy.ssafsound.domain.recruit.repository;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitLimitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitLimitationRepository extends JpaRepository<RecruitLimitation, Long> {
    List<RecruitLimitation> findByRecruitId(Long recruitId);

    void deleteAllByRecruit(@Param("recruit") Recruit recruit);
}
