package com.ssafy.ssafsound.domain.recruitcomment.repository;

import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitCommentRepository extends JpaRepository<RecruitComment, Long> {

    @Query("select r from recruit_comment r left join fetch r.member as m left join fetch m.majorType where r.recruit.id = :recruitId")
    List<RecruitComment> findByRecruitIdFetchJoinMemberAndReplies(Long recruitId);
}
