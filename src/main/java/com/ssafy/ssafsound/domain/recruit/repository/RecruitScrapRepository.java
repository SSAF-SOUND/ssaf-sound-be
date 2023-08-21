package com.ssafy.ssafsound.domain.recruit.repository;

import com.ssafy.ssafsound.domain.recruit.domain.RecruitScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruitScrapRepository extends JpaRepository<RecruitScrap, Long> {
    Optional<RecruitScrap> findByRecruitIdAndMemberId(Long recruitId, Long memberId);

    Long countByRecruitId(Long recruitId);

    Boolean existsByRecruitIdAndMemberId(Long recruitId, Long memberId);
}
