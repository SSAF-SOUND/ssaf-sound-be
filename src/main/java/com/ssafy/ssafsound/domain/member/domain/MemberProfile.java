package com.ssafy.ssafsound.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="member_profile")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfile {

    @Id
    @Column(name = "member_id")
    private Long id;

    @Column
    @Builder.Default
    private String introduce = "";

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name="member_id")
    private Member member;

    public void changeSelfIntroduction(String selfIntroduction) {
        this.introduce = selfIntroduction;
    }
}
