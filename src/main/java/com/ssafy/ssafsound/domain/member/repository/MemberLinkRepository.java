package com.ssafy.ssafsound.domain.member.repository;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberLinkRepository extends JpaRepository<MemberLink, Long> {

    @Modifying
    @Query("DELETE FROM  member_link ml WHERE ml.member = :member")
    void deleteMemberLinksByMember(@Param("member") Member member);
}
