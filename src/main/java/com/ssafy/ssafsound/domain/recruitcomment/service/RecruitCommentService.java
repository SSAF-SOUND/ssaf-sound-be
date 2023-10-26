package com.ssafy.ssafsound.domain.recruitcomment.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitComment;
import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitCommentLike;
import com.ssafy.ssafsound.domain.recruitcomment.dto.GetRecruitCommentsResDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PatchRecruitCommentReqDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentLikeResDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentReqDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentResDto;
import com.ssafy.ssafsound.domain.recruitcomment.repository.RecruitCommentLikeRepository;
import com.ssafy.ssafsound.domain.recruitcomment.repository.RecruitCommentRepository;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Long commentGroupId = dto.getCommentGroup();
        if(commentGroupId != -1) {
            recruitComment.setCommentGroup(recruitCommentRepository.getReferenceById(dto.getCommentGroup()));
        }
        recruitComment.setRecruit(recruitRepository.getReferenceById(recruitId));
        recruitComment.setWriter(writer);
        recruitCommentRepository.save(recruitComment);

        if(commentGroupId == -1) {
            commentGroupId = recruitComment.getId();
            recruitCommentRepository.updateCommentGroup(commentGroupId);
        }
        return PostRecruitCommentResDto.to(recruitComment, commentGroupId);
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
    public PostRecruitCommentLikeResDto toggleRecruitCommentLike(Long recruitCommentId, Long memberId) {
        RecruitCommentLike recruitCommentLike = recruitCommentLikeRepository
                .findByRecruitCommentIdAndMemberId(recruitCommentId, memberId).orElse(null);

        int likeCount = recruitCommentLikeRepository.countById(recruitCommentId);
        boolean liked = isPreExistRecruitCommentLike(recruitCommentId, memberId, recruitCommentLike);
        return new PostRecruitCommentLikeResDto(likeCount, liked);
    }

    @Transactional(readOnly = true)
    public GetRecruitCommentsResDto getRecruitComments(Long recruitId, Long memberId) {
        Recruit recruit = recruitRepository.findById(recruitId).orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));
        List<RecruitComment> recruitComments = recruitCommentRepository.findByRecruitIdFetchJoinMemberAndReplies(recruitId);
        Map<Long, Integer> likedCountMap = new HashMap<>();
        Map<Long, Boolean> memberLikedMap = new HashMap<>();

        recruitComments.forEach(recruitComment->{
            Long recruitCommentId = recruitComment.getId();
            likedCountMap.put(recruitCommentId, recruitCommentLikeRepository.countById(recruitCommentId));
        });

        if(memberId != null) {
            List<RecruitCommentLike> recruitCommentLikes = recruitCommentLikeRepository.findByRecruitCommentRecruitAndMemberIdFetchRecruitComment(recruit, memberId);
            recruitCommentLikes.forEach(recruitCommentLike -> memberLikedMap.put(recruitCommentLike.getRecruitComment().getId(), true));
        }
        return GetRecruitCommentsResDto.of(recruitComments, likedCountMap, memberLikedMap, memberId);
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
