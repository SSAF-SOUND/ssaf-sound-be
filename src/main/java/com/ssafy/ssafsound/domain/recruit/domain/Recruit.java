package com.ssafy.ssafsound.domain.recruit.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="recruit")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recruit extends BaseTimeEntity {

    @Id
    @Column(name = "recruit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long view;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column
    private String title;

    @Column
    private String content;

    @Column(name = "recruit_start")
    private LocalDateTime startDateTime;

    @Column(name = "recruit_end")
    private LocalDateTime endDateTime;

    @Column
    private Boolean deletedRecruit;

    @Column
    private Boolean finishedRecruit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
