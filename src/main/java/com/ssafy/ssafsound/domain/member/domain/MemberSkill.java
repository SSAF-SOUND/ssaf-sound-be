package com.ssafy.ssafsound.domain.member.domain;

import com.ssafy.ssafsound.domain.meta.domain.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="member_skill")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSkill {

    @Id
    @Column(name = "member_skill_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private Skill skill;
}
