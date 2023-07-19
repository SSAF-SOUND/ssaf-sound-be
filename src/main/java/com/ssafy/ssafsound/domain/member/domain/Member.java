package com.ssafy.ssafsound.domain.member.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.member.dto.PatchMemberDefaultInfoReqDto;
import com.ssafy.ssafsound.domain.member.dto.PostMemberInfoReqDto;
import com.ssafy.ssafsound.domain.member.dto.PutMemberLink;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column
    @Builder.Default
    private Integer certificationInquiryCount = 0;

    @Column
    private LocalDateTime certificationTryTime;

    @Column
    private Boolean major;

    @Column
    @Builder.Default
    private Boolean publicPortfolio = true;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private Set<MemberLink> memberLinks = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private Set<MemberSkill> memberSkills = new HashSet<>();

    @PreUpdate
    public void preUpdateCertificationTryTime() {
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

    public void setMemberLinks(List<PutMemberLink> memberLinks) {
        memberLinks.forEach(memberLink -> {
            this.memberLinks.add(MemberLink.builder()
                    .member(this)
                    .linkName(memberLink.getLinkName())
                    .path(memberLink.getPath())
                    .build());
        });
    }

    public void setMemberSkills(List<String> memberSkills, MetaDataConsumer consumer) {
        memberSkills.forEach(memberSkill -> {
            this.memberSkills.add(MemberSkill.builder()
                    .member(this)
                    .skill(consumer.getMetaData(MetaDataType.SKILL.name(), memberSkill))
                    .build());
        });
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

    public void exchangeDefaultInformation(
            PatchMemberDefaultInfoReqDto patchMemberDefaultInfoReqDto,
            MetaDataConsumer metaDataConsumer) {
        if (patchMemberDefaultInfoReqDto.getSsafyMember()) {
            this.ssafyMember = true;
            this.semester = patchMemberDefaultInfoReqDto.getSemester();
            this.campus = metaDataConsumer.getMetaData(MetaDataType.CAMPUS.name(), patchMemberDefaultInfoReqDto.getCampus());
        } else {
            this.ssafyMember = false;
        }
    }
}
