package com.ssafy.ssafsound.domain.recruit.domain;

import com.ssafy.ssafsound.domain.meta.converter.RecruitTypeConverter;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
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

    @Convert(converter = RecruitTypeConverter.class)
    private MetaData type;

    public void increaseCurrentNumber() {
        this.currentNumber++;
    }
}
