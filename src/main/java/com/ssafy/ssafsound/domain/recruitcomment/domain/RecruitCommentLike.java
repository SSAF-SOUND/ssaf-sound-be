package com.ssafy.ssafsound.domain.recruitcomment.domain;

import com.ssafy.ssafsound.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="recruit_comment_like")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitCommentLike {

    @Id
    @Column(name = "recruit_comment_like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_comment_id")
    private RecruitComment recruitComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
