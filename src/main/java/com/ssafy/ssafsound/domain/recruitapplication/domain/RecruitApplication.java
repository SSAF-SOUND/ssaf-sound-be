package com.ssafy.ssafsound.domain.recruitapplication.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.meta.converter.RecruitTypeConverter;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity(name="recruit_application")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "match_status != 'CANCEL'")
public class RecruitApplication extends BaseTimeEntity {

    @Id
    @Column(name = "recruit_application_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column
    @Builder.Default
    private Boolean isLike = Boolean.FALSE;

    public void changeStatus(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }

    public void toggleLike() {
        this.isLike = !this.isLike;
    }
}
