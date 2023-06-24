package com.ssafy.ssafsound.domain.member.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.meta.converter.CampusConverter;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="member")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer semester;

    @Column
    private String oauthIdentifier;

    @Column
    private String nickname;

    @Enumerated(EnumType.STRING)
    private AccountState accountState;

    @Enumerated(EnumType.STRING)
    private AuthenticationStatus certificationState;

    @Convert(converter = CampusConverter.class)
    private MetaData campus;

    @Enumerated(EnumType.STRING)
    private OAuthType oauthType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_role_id")
    private MemberRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_type_id")
    private MajorType majorType;

    @Column
    private Boolean ssafyMember;

    @Column
    private Boolean major;

    @Column
    private Boolean publicPortfolio;

    public void setMemberRole(MemberRole memberRole) {
        this.role = memberRole;
    }
}
