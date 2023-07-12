package com.ssafy.ssafsound.domain.member.repository;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLinkRepository extends JpaRepository<MemberLink, Long> {
    void deleteMemberLinksByMember(Member member);
}
