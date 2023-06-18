package com.ssafy.ssafsound.domain.member.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedUser;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.domain.MemberRole;
import com.ssafy.ssafsound.domain.member.dto.PostMemberReqDto;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;

    @Transactional
     public AuthenticatedUser createMemberByOauthIdentifier(PostMemberReqDto postMemberReqDto) {
        Member member = postMemberReqDto.createMember();
        MemberRole memberRole = findMemberRoleByRoleName("user");

        member.setMemberRole(memberRole);
        memberRepository.save(member);
        return AuthenticatedUser.builder()
                .memberId(member.getId())
                .memberRole(memberRole.getRoleType())
                .build();
     }

    public MemberRole findMemberRoleByRoleName(String roleType) {
        return memberRoleRepository.findByRoleType(roleType).orElseThrow(RuntimeException::new);
    }
}
