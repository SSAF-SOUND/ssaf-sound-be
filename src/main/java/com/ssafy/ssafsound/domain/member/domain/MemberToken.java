package com.ssafy.ssafsound.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="member_token")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberToken {

    @Id
    @Column(name = "member_id")
    private Long id;

    @Column
    private String accessToken;

    @Column
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name="member_id")
    private Member member;
}
