package com.ssafy.ssafsound.domain.recruit.repository;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    @Query("SELECT r FROM recruit r left join fetch r.limitations where r.id = :recruitId")
    Optional<Recruit> findByIdUsingFetchJoinRecruitLimitation(Long recruitId);

    @Query("SELECT r FROM recruit r left join fetch r.member left join fetch r.limitations where r.id = :recruitId")
    Optional<Recruit> findByIdUsingFetchJoinRegisterAndRecruitLimitation(Long recruitId);

    @Query("SELECT r FROM recruit r inner join fetch r.member where r.id = :recruitId")
    Recruit findByIdFetchJoinRegister(Long recruitId);
}
