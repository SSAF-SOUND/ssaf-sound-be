package com.ssafy.ssafsound.domain.recruitapplication.repository;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import com.ssafy.ssafsound.domain.recruitapplication.dto.RecruitApplicationElement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitApplicationRepository extends JpaRepository<RecruitApplication, Long> {
    @Query("SELECT r FROM recruit_application r left join fetch r.recruit left join r.recruit.member where r.id = :recruitApplicationId")
    Optional<RecruitApplication> findByIdFetchRecruitWriter(Long recruitApplicationId);

    @Query("SELECT r FROM recruit_application r left join r.member left join fetch r.recruit left join r.recruit.member where r.id = :recruitApplicationId")
    Optional<RecruitApplication> findByIdFetchParticipantAndRecruitWriter(Long recruitApplicationId);

    Optional<RecruitApplication> findByIdAndMemberId(Long recruitApplicationId, Long memberId);

    @Query("SELECT r FROM recruit_application r left join fetch r.member as m where r.recruit.id = :recruitId and r.matchStatus = :matchStatus")
    List<RecruitApplication> findByRecruitIdAndMatchStatusFetchMember(Long recruitId, MatchStatus matchStatus);

    @Query("SELECT new com.ssafy.ssafsound.domain.recruitapplication.dto.RecruitApplicationElement(r.recruit.id, r.id, r.type, r.matchStatus, m, rp.content, rq.content, r.isLike, r.createdAt, r.modifiedAt) from recruit_application r  " +
            "inner join r.member as m " +
            "left outer join recruit_question_reply as rp on r.id = rp.application.id " +
            "left outer join rp.question as rq " +
            "where r.recruit.id = :recruitId and r.recruit.member.id = :registerId and r.matchStatus = :matchStatus")
    List<RecruitApplicationElement> findByRecruitIdAndRegisterMemberAndMatchStatusIdWithQuestionReply(Long recruitId, Long registerId, MatchStatus matchStatus);

    @Query("SELECT new com.ssafy.ssafsound.domain.recruitapplication.dto.RecruitApplicationElement(r.recruit.id, r.id, r.type, r.matchStatus, m, rp.content, rq.content, r.isLike, r.createdAt, r.modifiedAt) from recruit_application r  " +
            "inner join r.member as m " +
            "left outer join recruit_question_reply as rp on r.id = rp.application.id " +
            "left outer join rp.question as rq " +
            "where r.id = :recruitApplicationId and r.recruit.member.id = :registerId")
    RecruitApplicationElement findByRecruitApplicationIdAndRegisterId(Long recruitApplicationId, Long registerId);

    @Query("SELECT ra FROM recruit_application ra join fetch ra.recruit as r join fetch ra.member where r.id in (:recruitId) and ra.matchStatus = com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus.DONE")
    List<RecruitApplication> findDoneRecruitApplicationByRecruitIdInFetchRecruitAndMember(List<Long> recruitId);

    List<RecruitApplication> findByRecruitIdAndMatchStatus(Long recruitId, MatchStatus matchStatus);

    void deleteByTypeNotIn(List<MetaData> recruitTypes);

    RecruitApplication findTopByRecruitIdAndMemberIdOrderByIdDesc(Long recruitId, Long memberId);

    @Modifying
    @Query(value = "update recruit_application ra "
        + "set ra.matchStatus = com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus.CANCEL "
        + "where ra.member.id = :memberId "
        + "  or ra.recruit.id in ("
        + "                        select r.id from recruit r where r.member.id = :memberId"
        + "                      )")
    void cancelAllByMemberId(Long memberId);
}
