package com.ssafy.ssafsound.domain.lunch.repository;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.domain.LunchPoll;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LunchPollRepository extends JpaRepository<LunchPoll, Long> {

//    LunchPoll findByMemberAndPolledAt(Member member, LocalDate polledAt);

    Optional<LunchPoll> findByMemberAndLunch(Member member, Lunch lunch);

    @Query( "SELECT" +
            "   lp " +
            "FROM " +
            "   lunch_poll lp " +
            "INNER JOIN " +
            "   lunch l " +
            "ON" +
            "   lp.lunch = l " +
            "AND" +
            "   lp.member = :member " +
            "AND " +
            "   l.campus = :campus " +
            "AND " +
            "   lp.polledAt = :polledAt")
    LunchPoll findByMemberAndCampusAndPolledAt(Member member, MetaData campus, LocalDate polledAt);
}
