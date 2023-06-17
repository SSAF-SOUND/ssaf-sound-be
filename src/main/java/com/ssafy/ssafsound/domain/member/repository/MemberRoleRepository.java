package com.ssafy.ssafsound.domain.member.repository;

import com.ssafy.ssafsound.domain.member.domain.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Integer> {
}
