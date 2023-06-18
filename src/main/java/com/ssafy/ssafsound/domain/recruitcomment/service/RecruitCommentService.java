package com.ssafy.ssafsound.domain.recruitcomment.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedUser;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitComment;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentReqDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentResDto;
import com.ssafy.ssafsound.domain.recruitcomment.repository.RecruitCommentRepository;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitCommentService {

    private final RecruitCommentRepository recruitCommentRepository;
    private final RecruitRepository recruitRepository;
    private final MemberRepository memberRepository;

    public PostRecruitCommentResDto saveRecruitComment(Long recruitId, AuthenticatedUser userInfo, PostRecruitCommentReqDto dto) {
        RecruitComment recruitComment = dto.toEntity();
        Member writer = memberRepository.findById(userInfo.getMemberId()).orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));
        RecruitComment commentGroup = (dto.getCommentGroup() == -1) ? recruitComment : recruitCommentRepository.getReferenceById(dto.getCommentGroup());

        recruitComment.setCommentGroup(commentGroup);
        recruitComment.setRecruit(recruitRepository.getReferenceById(recruitId));
        recruitComment.setWriter(writer);
        recruitCommentRepository.save(recruitComment);
        return PostRecruitCommentResDto.from(recruitComment);
    }

    public void deleteRecruitComment(Long recruitCommentId, AuthenticatedUser userInfo) {
        RecruitComment recruitComment = recruitCommentRepository.findById(recruitCommentId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        if(recruitComment.getMember().getId().equals(userInfo.getMemberId())) {
            recruitCommentRepository.delete(recruitComment);
        } else {
            throw new RecruitException(RecruitErrorInfo.NOT_AUTHORIZATION_MEMBER);
        }
    }
}
