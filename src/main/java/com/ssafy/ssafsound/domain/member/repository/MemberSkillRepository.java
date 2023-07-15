package com.ssafy.ssafsound.domain.member.repository;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberSkillRepository extends JpaRepository<MemberSkill, Long> {

    @Modifying
    @Query("DELETE FROM  member_skill ms WHERE ms.member = :member")
    void deleteMemberSkillsByMember(@Param("member") Member member);
}
