package com.ssafy.ssafsound.domain.lunch.domain;

import com.ssafy.ssafsound.domain.lunch.exception.LunchErrorInfo;
import com.ssafy.ssafsound.domain.lunch.exception.LunchException;
import com.ssafy.ssafsound.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name="lunch_poll")
@Getter
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

    @Builder
    public LunchPoll(Lunch lunch, Member member, LocalDate polledAt) {
        this.lunch = lunch;
        this.member = member;
        this.polledAt = polledAt;

        lunch.getLunchPolls().add(this);
    }

    public void setLunch(Lunch lunch){

        if (this.lunch != null){
            this.lunch.getLunchPolls().remove(this);
        }

        this.lunch = lunch;
        lunch.getLunchPolls().add(this);
    }

    public void deleteLunchPoll() {
        if (this.lunch != null) {
            this.lunch.getLunchPolls().remove(this);
        }
    }
}
