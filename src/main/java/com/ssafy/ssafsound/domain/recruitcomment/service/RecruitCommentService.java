package com.ssafy.ssafsound.domain.recruitcomment.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitComment;
import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitCommentLike;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PatchRecruitCommentReqDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentReqDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentResDto;
import com.ssafy.ssafsound.domain.recruitcomment.repository.RecruitCommentLikeRepository;
import com.ssafy.ssafsound.domain.recruitcomment.repository.RecruitCommentRepository;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitCommentService {

    private final RecruitCommentRepository recruitCommentRepository;
    private final RecruitRepository recruitRepository;
    private final RecruitCommentLikeRepository recruitCommentLikeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PostRecruitCommentResDto saveRecruitComment(Long recruitId, AuthenticatedMember userInfo, PostRecruitCommentReqDto dto) {
        RecruitComment recruitComment = dto.toEntity();
        Member writer = memberRepository.findById(userInfo.getMemberId()).orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));
        RecruitComment commentGroup = (dto.getCommentGroup() == -1) ? recruitComment : recruitCommentRepository.getReferenceById(dto.getCommentGroup());

        recruitComment.setCommentGroup(commentGroup);
        recruitComment.setRecruit(recruitRepository.getReferenceById(recruitId));
        recruitComment.setWriter(writer);
        recruitCommentRepository.save(recruitComment);
        return PostRecruitCommentResDto.from(recruitComment);
    }

    @Transactional
    public void deleteRecruitComment(Long recruitCommentId, AuthenticatedMember userInfo) {
        RecruitComment recruitComment = recruitCommentRepository.findById(recruitCommentId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        if(recruitComment.getMember().getId().equals(userInfo.getMemberId())) {
            recruitCommentRepository.delete(recruitComment);
        } else {
            throw new RecruitException(RecruitErrorInfo.NOT_AUTHORIZATION_MEMBER);
        }
    }

    @Transactional
    public RecruitComment updateRecruitComment(Long recruitCommentId, AuthenticatedMember userInfo, PatchRecruitCommentReqDto patchRecruitCommentReqDto) {
        RecruitComment recruitComment = recruitCommentRepository.findById(recruitCommentId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        if(recruitComment.getMember().getId().equals(userInfo.getMemberId())) {
            recruitComment.updateContent(patchRecruitCommentReqDto.getContent());
        } else {
            throw new RecruitException(RecruitErrorInfo.NOT_AUTHORIZATION_MEMBER);
        }
        return recruitComment;
    }

    @Transactional
    public boolean toggleRecruitCommentLike(Long recruitCommentId, Long memberId) {
        RecruitCommentLike recruitCommentLike = recruitCommentLikeRepository
                .findByRecruitCommentIdAndMemberId(recruitCommentId, memberId).orElse(null);
        return isPreExistRecruitCommentLike(recruitCommentId, memberId, recruitCommentLike);
    }

    private boolean isPreExistRecruitCommentLike(Long recruitCommentId, Long memberId, RecruitCommentLike recruitCommentLike) {
        if(recruitCommentLike != null) {
            recruitCommentLikeRepository.delete(recruitCommentLike);
            return true;
        } else {
            saveRecruitCommentLike(recruitCommentId, memberId);
            return false;
        }
    }

    private RecruitCommentLike saveRecruitCommentLike(Long recruitCommentId, Long memberId) {
        RecruitCommentLike recruitCommentLike = RecruitCommentLike.builder()
                .recruitComment(recruitCommentRepository.getReferenceById(recruitCommentId))
                .member(memberRepository.getReferenceById(memberId))
                .build();

        recruitCommentLikeRepository.save(recruitCommentLike);
        return recruitCommentLike;
    }
}
