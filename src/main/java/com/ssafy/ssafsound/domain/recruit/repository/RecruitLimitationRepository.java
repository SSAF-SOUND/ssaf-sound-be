package com.ssafy.ssafsound.domain.recruit.repository;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitLimitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitLimitationRepository extends JpaRepository<RecruitLimitation, Long> {
    List<RecruitLimitation> findByRecruitId(Long recruitId);

    void deleteAllByRecruit(@Param("recruit") Recruit recruit);

    @Modifying
    @Query(value = "update recruit_limitation rl " +
            "set rl.currentNumber = rl.currentNumber-1 " +
            "where (rl.recruit, rl.type) in " +
                "(" +
                "  select ra.recruit, ra.type from recruit_application ra " +
                "  where ra.matchStatus = com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus.DONE and ra.member.id = :memberId" +
                ")"
    )
    void decreaseCurrentNumberByMemberId(Long memberId);
}
