package com.ssafy.ssafsound.domain.lunch.domain;

import com.ssafy.ssafsound.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name="lunch_poll")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LunchPoll {

    @Id
    @Column(name = "lunch_poll_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lunch_id")
    private Lunch lunch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private LocalDate polledAt;
}
