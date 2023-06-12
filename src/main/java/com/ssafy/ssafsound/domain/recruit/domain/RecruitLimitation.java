package com.ssafy.ssafsound.domain.recruit.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="recruit_limitation")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitLimitation {

    @Id
    @Column(name = "recruit_limitation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer limitation;

    @Column
    private Integer currentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_type_id")
    private RecruitType type;
}
