package com.ssafy.ssafsound.domain.term.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "member_term_agreement")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberTermAgreement extends BaseTimeEntity {

    @Id
    @Column(name = "member_term_agreement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id")
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static List<MemberTermAgreement> ofMemberAndTerms(Member member, List<Term> terms) {
        return terms.stream()
                .map(term -> MemberTermAgreement.builder().member(member).term(term).build())
                .collect(Collectors.toList());
    }
}
