package com.ssafy.ssafsound.domain.member.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.member.dto.PostMemberInfoReqDto;
import com.ssafy.ssafsound.domain.meta.converter.CampusConverter;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="member")
@Table(indexes = @Index(name = "nickname_index", columnList = "nickname", unique = true))
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

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private AuthenticationStatus certificationState = AuthenticationStatus.UNCERTIFIED;

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

    public void setCertificationState(AuthenticationStatus certified) {
        this.certificationState = certified;
    }

    public void setSSAFYMemberInformation(PostMemberInfoReqDto postMemberInfoReqDto, MetaDataConsumer consumer) {
        this.nickname = postMemberInfoReqDto.getNickname();
        this.ssafyMember = postMemberInfoReqDto.getSsafyMember();
        this.semester = postMemberInfoReqDto.getSemester();
        this.major = postMemberInfoReqDto.getIsMajor();
        this.campus = consumer.getMetaData(MetaDataType.CAMPUS.name(), postMemberInfoReqDto.getCampus());
    }

    public void setGeneralMemberInformation(PostMemberInfoReqDto postMemberInfoReqDto) {
        this.nickname = postMemberInfoReqDto.getNickname();
        this.ssafyMember = postMemberInfoReqDto.getSsafyMember();
        this.major = postMemberInfoReqDto.getIsMajor();
    }
}
