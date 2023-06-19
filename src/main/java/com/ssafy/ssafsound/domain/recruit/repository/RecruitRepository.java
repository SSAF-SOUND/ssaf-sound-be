package com.ssafy.ssafsound.domain.recruit.repository;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    @Query("SELECT r FROM recruit r left join fetch r.limitations")
    Optional<Recruit> findByIdUsingFetchJoinRecruitLimitation(Long recruitId);
}
