package com.ssafy.ssafsound.domain.recruitapplication.service;

import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import com.ssafy.ssafsound.domain.recruitapplication.dto.PostRecruitApplicationReqDto;
import com.ssafy.ssafsound.domain.recruitapplication.repository.RecruitApplicationRepository;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitApplicationService {

    private final RecruitRepository recruitRepository;
    private final RecruitApplicationRepository recruitApplicationRepository;
    private final MemberRepository memberRepository;
    private final MetaDataConsumer metaDataConsumer;
    @Transactional
    public RecruitApplication saveRecruitApplication(Long recruitId, Long memberId, PostRecruitApplicationReqDto dto) {
        Recruit recruit = recruitRepository.findByIdUsingFetchJoinRecruitLimitation(recruitId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        String recruitType = dto.getRecruitType();
        if(!isRecruitingType(recruitType, recruit)) {
            throw new RecruitException(RecruitErrorInfo.NOT_RECRUITING_TYPE);
        }

        RecruitApplication recruitApplication = dto.createRecruitApplicationFromPredefinedMetadata(
                memberRepository.getReferenceById(memberId),
                recruit,
                metaDataConsumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), recruitType));

        recruitApplicationRepository.save(recruitApplication);
        return recruitApplication;
    }

    @Transactional
    public RecruitApplication approveRecruitApplicationByRegister(Long recruitApplicationId, Long memberId, MatchStatus status) {
        RecruitApplication recruitApplication = recruitApplicationRepository.findByIdFetchRecruitWriter(recruitApplicationId)
                .orElseThrow(() -> new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        return changeRecruitApplicationState(recruitApplication, memberId, status,
                (entity, mid) -> {
                    boolean isNotRegister = !entity.getRecruit().getMember().getId().equals(mid);
                    boolean isNotValidMatchStatus = !entity.getMatchStatus().equals(MatchStatus.WAITING_REGISTER_APPROVE);
                    return isNotRegister || isNotValidMatchStatus;
                });
    }

    @Transactional
    public RecruitApplication joinRecruitApplication(Long recruitApplicationId, Long memberId, MatchStatus status) {
        RecruitApplication recruitApplication = recruitApplicationRepository.findByIdAndMemberId(recruitApplicationId, memberId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        return changeRecruitApplicationState(recruitApplication, memberId, status,
                (entity, mid)-> !entity.getMatchStatus().equals(MatchStatus.WAITING_APPLICANT));
    }

    @Transactional
    public RecruitApplication rejectRecruitApplication(Long recruitApplicationId, Long memberId, MatchStatus status) {
        RecruitApplication recruitApplication = recruitApplicationRepository.findByIdAndMemberIdFetchRecruitWriter(recruitApplicationId, memberId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        return changeRecruitApplicationState(recruitApplication, memberId, status,
                (entity, mid)-> {
                    boolean isNotRegisterAndParticipants = (!entity.getMember().getId().equals(mid) && !entity.getRecruit().getMember().getId().equals(mid));
                    boolean isNotValidMatchStatus = (!entity.getMatchStatus().equals(MatchStatus.WAITING_REGISTER_APPROVE) && !entity.getMatchStatus().equals(MatchStatus.WAITING_APPLICANT));
                    return isNotRegisterAndParticipants || isNotValidMatchStatus;
                });
    }

    private RecruitApplication changeRecruitApplicationState(RecruitApplication recruitApplication, Long memberId,
                                                             MatchStatus status, RecruitApplicationValidator validator) {
        if(validator.hasError(recruitApplication, memberId)) {
            throw new RecruitException(RecruitErrorInfo.INVALID_CHANGE_MEMBER_OPERATION);
        }
        recruitApplication.changeStatus(status);
        return recruitApplication;
    }

    private boolean isRecruitingType(String recruitType, Recruit recruit) {
        return recruit.getLimitations().stream()
                .anyMatch(limit -> limit.getType().getName().equals(recruitType));
    }
}
