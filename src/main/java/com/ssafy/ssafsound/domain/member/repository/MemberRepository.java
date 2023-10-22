package com.ssafy.ssafsound.domain.member.repository;

import com.ssafy.ssafsound.domain.member.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByOauthIdentifier(String oauthIdentifier);
    
    boolean existsByNickname(String nickname);

    @EntityGraph(attributePaths = {"memberLinks", "memberSkills"})
    @Query("select m from member m where m.id = :memberId")
    Optional<Member> findWithMemberLinksAndMemberSkills(@Param("memberId") Long memberId);
}
