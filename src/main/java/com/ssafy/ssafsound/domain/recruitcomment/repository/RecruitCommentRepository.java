package com.ssafy.ssafsound.domain.recruitcomment.repository;

import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitCommentRepository extends JpaRepository<RecruitComment, Long> {
}
