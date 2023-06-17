package com.ssafy.ssafsound.domain.member.repository;

import com.ssafy.ssafsound.domain.member.domain.OAuthType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthTypeRepository extends JpaRepository<OAuthType, Integer> {
}
