package com.ssafy.ssafsound.domain.recruit.repository;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecruitSkillRepository extends JpaRepository<RecruitSkill, Long> {
    @Modifying
    @Query("DELETE FROM  recruit_skill rs WHERE rs.recruit = :recruit")
    void deleteAllByRecruit(@Param("recruit") Recruit recruit);
}
