package com.ssafy.ssafsound.domain.member.repository;

import com.ssafy.ssafsound.domain.member.domain.MemberToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {
}
