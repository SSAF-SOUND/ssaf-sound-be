package com.ssafy.ssafsound.domain.recruit.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="recruit_type")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitType {

    @Id
    @Column(name = "recruit_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 32)
    private String name;
}
