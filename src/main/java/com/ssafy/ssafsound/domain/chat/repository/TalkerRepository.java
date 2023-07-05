package com.ssafy.ssafsound.domain.chat.repository;

import com.ssafy.ssafsound.domain.chat.domain.Talker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalkerRepository extends JpaRepository<Talker, Long> {
}
