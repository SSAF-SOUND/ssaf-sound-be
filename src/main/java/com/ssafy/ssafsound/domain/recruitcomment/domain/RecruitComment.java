package com.ssafy.ssafsound.domain.recruitcomment.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name="recruit_comment")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitComment extends BaseTimeEntity {

    @Id
    @Column(name = "recruit_comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @Column
    private Boolean deletedComment;

    @OneToMany(mappedBy = "commentGroup")
    List<RecruitComment> replies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_group", referencedColumnName = "recruit_comment_id", updatable = false)
    private RecruitComment commentGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
