package com.ssafy.ssafsound.domain.member.repository;

import com.ssafy.ssafsound.domain.member.domain.MemberLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLoginLogRepository extends JpaRepository<MemberLoginLog, Long> {
}
