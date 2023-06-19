package com.ssafy.ssafsound.domain.recruit.repository;

import com.ssafy.ssafsound.domain.recruit.domain.RecruitApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruitApplicationRepository extends JpaRepository<RecruitApplication, Long> {
    @Query("SELECT r FROM recruit_application r left join fetch r.recruit left join r.recruit.member where r.id = :recruitApplicationId")
    Optional<RecruitApplication> findByIdFetchRecruitWriter(Long recruitApplicationId);

    @Query("SELECT r FROM recruit_application r left join fetch r.recruit left join r.recruit.member where r.id = :recruitApplicationId and r.member.id = :memberId")
    Optional<RecruitApplication> findByIdAndMemberIdFetchRecruitWriter(Long recruitApplicationId, Long memberId);

    Optional<RecruitApplication> findByIdAndMemberId(Long recruitApplicationId, Long memberId);
}
