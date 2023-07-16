package com.ssafy.ssafsound.domain.member.repository;

import com.ssafy.ssafsound.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthIdentifier(String oauthIdentifier);
    boolean existsByNickname(String nickname);
    @Query("select m from member m join fetch m.memberLinks join fetch m.memberSkills where m.id = :memberId")
    Optional<Member> findWithMemberLinksAndMemberSkills(@Param("memberId") Long memberId);
}
