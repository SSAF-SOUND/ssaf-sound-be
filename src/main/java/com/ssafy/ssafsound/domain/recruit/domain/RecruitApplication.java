package com.ssafy.ssafsound.domain.recruit.domain;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.meta.converter.RecruitTypeConverter;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="recruit_application")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitApplication {

    @Id
    @Column(name = "recruit_application_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean publicProfile;

    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

    @Convert(converter = RecruitTypeConverter.class)
    private MetaData type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
