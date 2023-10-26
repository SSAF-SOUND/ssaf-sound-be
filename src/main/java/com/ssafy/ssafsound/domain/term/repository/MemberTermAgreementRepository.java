package com.ssafy.ssafsound.domain.term.repository;

import com.ssafy.ssafsound.domain.term.domain.MemberTermAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTermAgreementRepository extends JpaRepository<MemberTermAgreement, Long> {
}
