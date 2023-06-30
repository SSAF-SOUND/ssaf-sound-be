package com.ssafy.ssafsound.domain.lunch.repository;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.domain.LunchPoll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LunchPollRepository extends JpaRepository<LunchPoll, Long> {

    LunchPoll findByMember_IdAndPolledAt(Long memberId, LocalDate polledAt);

    @Modifying
    @Query(value = "insert into lunch_poll (lunch_id,member_id,polled_at) values(:lunchId, :memberId, :polledAt)", nativeQuery = true)
    void saveByMember_IdAndLunch_Id(Long memberId, Long lunchId, LocalDate polledAt);

    Optional<LunchPoll> findByMember_IdAndLunch(Long memberId, Lunch lunch);
}
