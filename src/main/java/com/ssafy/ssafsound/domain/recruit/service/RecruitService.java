package com.ssafy.ssafsound.domain.recruit.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.recruit.domain.*;
import com.ssafy.ssafsound.domain.recruit.dto.PostRecruitApplicationReqDto;
import com.ssafy.ssafsound.domain.recruit.dto.PostRecruitReqDto;
import com.ssafy.ssafsound.domain.recruit.dto.RecruitLimitElement;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitApplicationRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitLimitationRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitScrapRepository;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final RecruitLimitationRepository recruitLimitationRepository;
    private final RecruitApplicationRepository recruitApplicationRepository;
    private final RecruitScrapRepository recruitScrapRepository;
    private final MemberRepository memberRepository;
    private final MetaDataConsumer metaDataConsumer;

    @Transactional
    public Recruit saveRecruit(AuthenticatedMember userInfo, PostRecruitReqDto postRecruitReqDto) {
        Recruit recruit = postRecruitReqDto.createRecruitFromPredefinedMetadata(metaDataConsumer);
        Member register = memberRepository.findById(userInfo.getMemberId()).orElseThrow(RuntimeException::new);
        recruit.setRegister(register);
        recruitRepository.save(recruit);

        String registerRecruitType = postRecruitReqDto.getRegisterRecruitType();
        recruitApplicationRepository.save(createRegisterRecruitApplication(recruit, register, registerRecruitType));
        recruitLimitationRepository.saveAll(createRecruitLimitations(recruit, postRecruitReqDto.getLimitations(), registerRecruitType));
        return recruit;
    }

    @Transactional
    public boolean toggleRecruitScrap(Long recruitId, Long memberId) {
        RecruitScrap recruitScrap = recruitScrapRepository.findByRecruitIdAndMemberId(recruitId, memberId)
                .orElse(null);

        return isPreExistRecruitScrap(recruitId, memberId, recruitScrap);
    }

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

    private boolean isPreExistRecruitScrap(Long recruitId, Long memberId, RecruitScrap recruitScrap) {
        if(recruitScrap != null) {
            recruitScrapRepository.delete(recruitScrap);
            return true;
        } else {
            saveRecruitScrap(recruitId, memberId);
            return false;
        }
    }

    private void saveRecruitScrap(Long recruitId, Long memberId) {
        RecruitScrap recruitScrap;
        recruitScrap = RecruitScrap.builder()
                .member(memberRepository.getReferenceById(memberId))
                .recruit(recruitRepository.getReferenceById(recruitId))
                .build();
        recruitScrapRepository.save(recruitScrap);
    }

    private RecruitApplication createRegisterRecruitApplication(Recruit recruit, Member register, String registerRecruitType) {
        RecruitApplication application = RecruitApplication.builder()
                .recruit(recruit)
                .member(register)
                .type(metaDataConsumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), registerRecruitType))
                .matchStatus(MatchStatus.DONE)
                .build();

        recruit.addApplications(application);
        return application;
    }

    private List<RecruitLimitation> createRecruitLimitations(Recruit recruit, List<RecruitLimitElement> limits, String registerRecruitType) {
        List<RecruitLimitation> limitations =  limits.stream().map((limit)->
                RecruitLimitation.builder()
                        .recruit(recruit)
                        .type(metaDataConsumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), limit.getRecruitType()))
                        .limitation(limit.getLimit())
                        .currentNumber(limit.getRecruitType().equals(registerRecruitType) ? 1 : 0)
                        .build())
                .collect(Collectors.toList());

        recruit.setRecruitLimitations(limitations);
        return limitations;
    }
}
