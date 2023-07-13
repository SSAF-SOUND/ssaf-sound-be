package com.ssafy.ssafsound.domain.recruit.repository;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long>, RecruitDynamicQueryRepository {
    @Query("SELECT r FROM recruit r left join fetch r.limitations where r.id = :recruitId")
    Optional<Recruit> findByIdUsingFetchJoinRecruitLimitation(Long recruitId);

    @Query("SELECT r FROM recruit r left join fetch r.member left join fetch r.limitations where r.id = :recruitId")
    Optional<Recruit> findByIdUsingFetchJoinRegisterAndRecruitLimitation(Long recruitId);

    @Query("SELECT r FROM recruit r inner join fetch r.member where r.id = :recruitId")
    Recruit findByIdFetchJoinRegister(Long recruitId);

    @Query("SELECT r FROM recruit r left join fetch r.member where r.id = :recruitId")
    Optional<Recruit> findByIdUsingFetchJoinRegister(Long recruitId);

    @Modifying
    @Query(value = "update recruit r set r.finishedRecruit = true where r.endDateTime < :todayMaxTime")
    int expiredTimeOutRecruits(LocalDateTime todayMaxTime);
}
