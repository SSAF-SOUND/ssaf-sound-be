package com.ssafy.ssafsound.domain.member.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.member.dto.PostMemberInfoReqDto;
import com.ssafy.ssafsound.domain.meta.converter.CampusConverter;
import com.ssafy.ssafsound.domain.meta.converter.MajorTrackConverter;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Convert(converter = MajorTrackConverter.class)
    private MetaData majorTrack;

    @Column
    private Boolean ssafyMember;

    @Builder.Default
    @Column
    private Integer certificationInquiryCount = 0;

    @Column
    private LocalDateTime certificationTryTime;

    @Column
    private Boolean major;

    @Column
    private Boolean publicPortfolio;

    @PreUpdate
    public void preUpdate() {
        this.certificationTryTime = LocalDateTime.now();
    }

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

    public void setMajorTrack(MetaData majorTrack) {
        this.majorTrack = majorTrack;
    }

    public void increaseCertificationInquiryCount() {
        this.certificationInquiryCount += 1;
    }

    public void initializeCertificationInquiryCount() {
        this.certificationInquiryCount = 0;
    }

    public void setGeneralMemberInformation(PostMemberInfoReqDto postMemberInfoReqDto) {
        this.nickname = postMemberInfoReqDto.getNickname();
        this.ssafyMember = postMemberInfoReqDto.getSsafyMember();
        this.major = postMemberInfoReqDto.getIsMajor();
    }
}
