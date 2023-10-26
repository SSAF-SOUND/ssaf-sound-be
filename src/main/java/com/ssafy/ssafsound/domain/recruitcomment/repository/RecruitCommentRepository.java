package com.ssafy.ssafsound.domain.recruitcomment.repository;

import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitCommentRepository extends JpaRepository<RecruitComment, Long> {

    @Query("select r from recruit_comment r left join fetch r.member as m where r.recruit.id = :recruitId")
    List<RecruitComment> findByRecruitIdFetchJoinMemberAndReplies(Long recruitId);

    @Modifying
    @Query(value = "update recruit_comment " +
            "set comment_group = :id " +
            "where recruit_comment_id = :id", nativeQuery = true)
    void updateCommentGroup(Long id);
}