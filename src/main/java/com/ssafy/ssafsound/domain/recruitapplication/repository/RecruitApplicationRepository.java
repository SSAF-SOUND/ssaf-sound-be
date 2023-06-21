package com.ssafy.ssafsound.domain.recruitapplication.repository;

import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruitApplicationRepository extends JpaRepository<RecruitApplication, Long> {
    @Query("SELECT r FROM recruit_application r left join fetch r.recruit left join r.recruit.member where r.id = :recruitApplicationId")
    Optional<RecruitApplication> findByIdFetchRecruitWriter(Long recruitApplicationId);

    @Query("SELECT r FROM recruit_application r left join r.member left join fetch r.recruit left join r.recruit.member where r.id = :recruitApplicationId")
    Optional<RecruitApplication> findByIdAndMemberIdFetchRecruitWriter(Long recruitApplicationId);

    Optional<RecruitApplication> findByIdAndMemberId(Long recruitApplicationId, Long memberId);
}
