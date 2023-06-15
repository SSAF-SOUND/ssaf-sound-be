package com.ssafy.ssafsound.domain.recruit.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="recruit_skill")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitSkill {

    @Id
    @Column(name = "recruit_skill_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

    @Column
    private String skill;
}
