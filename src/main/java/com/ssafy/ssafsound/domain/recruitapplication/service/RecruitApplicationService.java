package com.ssafy.ssafsound.domain.recruitapplication.service;

import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitLimitation;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitQuestion;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitQuestionReply;
import com.ssafy.ssafsound.domain.recruit.dto.GetRejectedRecruitApplicationsResDto;
import com.ssafy.ssafsound.domain.recruit.dto.PatchRecruitApplicationStatusResDto;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitLimitationRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitQuestionReplyRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import com.ssafy.ssafsound.domain.recruitapplication.dto.*;
import com.ssafy.ssafsound.domain.recruitapplication.repository.RecruitApplicationComplexQueryRepository;
import com.ssafy.ssafsound.domain.recruitapplication.repository.RecruitApplicationRepository;
import com.ssafy.ssafsound.domain.recruitapplication.validator.RecruitApplicationValidator;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitApplicationLikeResDto;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitApplicationService {

    private final RecruitRepository recruitRepository;
    private final RecruitQuestionReplyRepository recruitQuestionReplyRepository;
    private final RecruitApplicationRepository recruitApplicationRepository;
    private final RecruitApplicationComplexQueryRepository recruitApplicationComplexQueryRepository;
    private final RecruitLimitationRepository recruitLimitationRepository;
    private final MemberRepository memberRepository;
    private final MetaDataConsumer metaDataConsumer;

    @Transactional
    public PatchRecruitApplicationStatusResDto saveRecruitApplication(Long recruitId, Long memberId, PostRecruitApplicationReqDto postRecruitApplicationReqDto) {
        Recruit recruit = recruitRepository.findByIdUsingFetchJoinRecruitLimitation(recruitId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));
        String recruitType = postRecruitApplicationReqDto.getRecruitType();
        if(!isRecruitingType(recruitType, recruit)) {
            throw new RecruitException(RecruitErrorInfo.NOT_RECRUITING_TYPE);
        }

        if(isPrevExistWaitingOrDoneMember(recruitId, memberId)) {
            throw new RecruitException(RecruitErrorInfo.PREV_EXIST_MEMBER_APPLICATION);
        }

        RecruitApplication recruitApplication = postRecruitApplicationReqDto.createRecruitApplicationFromPredefinedMetadata(
                memberRepository.getReferenceById(memberId),
                recruit,
                metaDataConsumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), recruitType));
        List<RecruitQuestionReply> participantAnswers = makeRecruitQuestionReplies(postRecruitApplicationReqDto, recruit, recruitApplication);
        recruitApplicationRepository.save(recruitApplication);
        recruitQuestionReplyRepository.saveAll(participantAnswers);
        return new PatchRecruitApplicationStatusResDto(recruitApplication.getId(), MatchStatus.PENDING.name());
    }

    @Transactional
    public PatchRecruitApplicationStatusResDto approveRecruitApplicationByRegister(Long recruitApplicationId, Long memberId, MatchStatus status) {
        RecruitApplication recruitApplication = recruitApplicationRepository.findByIdFetchRecruitWriter(recruitApplicationId)
                .orElseThrow(() -> new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        getNotFullRecruitLimitation(recruitApplication.getRecruit(), recruitApplication.getType());
        return changeRecruitApplicationState(recruitApplication, memberId, status,
                (entity, mid) -> {
                    boolean isNotRegister = !entity.getRecruit().getMember().getId().equals(mid);
                    boolean isNotValidMatchStatus = !entity.getMatchStatus().equals(MatchStatus.PENDING);
                    return isNotRegister || isNotValidMatchStatus;
                });
    }

    @Transactional
    public PatchRecruitApplicationStatusResDto rejectRecruitApplication(Long recruitApplicationId, Long memberId, MatchStatus status) {
        RecruitApplication recruitApplication = recruitApplicationRepository.findByIdFetchParticipantAndRecruitWriter(recruitApplicationId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        return changeRecruitApplicationState(recruitApplication, memberId, status,
                (entity, mid)-> {
                    boolean isNotValidRegisterAndState = (!entity.getRecruit().getMember().getId().equals(mid) || !entity.getMatchStatus().equals(MatchStatus.PENDING));
                    return isNotValidRegisterAndState;
                });
    }

    @Transactional
    public PatchRecruitApplicationStatusResDto cancelRecruitApplication(Long recruitApplicationId, Long memberId, MatchStatus status) {
        RecruitApplication recruitApplication = recruitApplicationRepository.findByIdFetchParticipantAndRecruitWriter(recruitApplicationId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        return changeRecruitApplicationState(recruitApplication, memberId, status,
                (entity, mid)-> {
                    boolean isNotValidParticipantCancel = (!entity.getMember().getId().equals(memberId)
                            || (entity.getMatchStatus() != MatchStatus.PENDING));
                    boolean isNotValidRegisterCancel = (!entity.getRecruit().getMember().getId().equals(memberId) ||
                            entity.getMatchStatus() != MatchStatus.DONE);
                    return isNotValidParticipantCancel && isNotValidRegisterCancel;
                }
        );
    }

    @Transactional(readOnly = true)
    public GetRecruitParticipantsResDto getRecruitParticipants(Long recruitId) {
        List<RecruitApplication> recruitApplications = recruitApplicationRepository
                .findByRecruitIdAndMatchStatusFetchMember(recruitId, MatchStatus.DONE);

        List<RecruitLimitation> recruitLimitations = recruitLimitationRepository.findByRecruitId(recruitId);

        GetRecruitParticipantsResDto getRecruitParticipantsResDto = GetRecruitParticipantsResDto.of(recruitApplications, recruitLimitations);
        getRecruitParticipantsResDto.addRegisterInfo(recruitRepository.findByIdFetchJoinRegister(recruitId));
        return getRecruitParticipantsResDto;
    }

    @Transactional(readOnly = true)
    public GetRecruitApplicationsResDto getRecruitApplications(Long recruitId, Long memberId) {
        Recruit recruit = recruitRepository.findById(recruitId).orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));
        return new GetRecruitApplicationsResDto(recruit, recruitApplicationRepository.findByRecruitIdAndRegisterMemberAndMatchStatusIdWithQuestionReply(recruitId, memberId, MatchStatus.PENDING));
    }

    @Transactional
    public PostRecruitApplicationLikeResDto toggleRecruitApplicationLike(Long recruitApplicationId, Long memberId) {
        RecruitApplication recruitApplication = recruitApplicationRepository.findByIdFetchRecruitWriter(recruitApplicationId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        if(!recruitApplication.getRecruit().getMember().getId().equals(memberId)) throw new RecruitException(RecruitErrorInfo.INVALID_CHANGE_MEMBER_OPERATION);
        recruitApplication.toggleLike();

        return new PostRecruitApplicationLikeResDto(recruitApplication.getIsLike());
    }

    @Transactional(readOnly = true)
    public GetRecruitApplicationDetailResDto getRecruitApplicationByIdAndRegisterId(Long recruitApplicationId, Long registerId) {
        return new GetRecruitApplicationDetailResDto(recruitApplicationRepository.findByRecruitApplicationIdAndRegisterId(recruitApplicationId, registerId));
    }

    @Transactional(readOnly = true)
    public GetRejectedRecruitApplicationsResDto getRejectedRecruitApplicationByRecruitId(Long recruitId, Long memberId) {
        Recruit recruit = recruitRepository.findByIdFetchJoinRegister(recruitId);

        if(recruit==null || !recruit.getMember().getId().equals(memberId)) {
            throw new RecruitException(RecruitErrorInfo.NOT_VALID_REGISTER_READ_REJECT_REQUEST);
        }

        List<RecruitApplicationElement> rejectedApplications = recruitApplicationRepository.findByRecruitIdAndRegisterMemberAndMatchStatusIdWithQuestionReply(recruitId, memberId, MatchStatus.REJECT);
        return new GetRejectedRecruitApplicationsResDto(recruit, rejectedApplications);
    }

    @Transactional(readOnly = true)
    public GetRecruitApplicationDetailResDto getRecentPendingRecruitApplicationByRecruitId(Long recruitId, Long memberId) {
        memberRepository.findById(memberId).orElseThrow(()->new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));
        return new GetRecruitApplicationDetailResDto(recruitApplicationComplexQueryRepository.findTopByRecruitIdAndMemberId(recruitId, memberId));
    }

    private boolean isPrevExistWaitingOrDoneMember(Long recruitId, Long memberId) {
        RecruitApplication recentRecruitApplication = recruitApplicationRepository.findTopByRecruitIdAndMemberIdOrderByIdDesc(recruitId, memberId);
        if(recentRecruitApplication == null) return false;

        boolean prevWaitingMember = recentRecruitApplication.getMatchStatus().equals(MatchStatus.PENDING);
        boolean prevDoneMember = recentRecruitApplication.getMatchStatus().equals(MatchStatus.DONE);
        return (prevWaitingMember || prevDoneMember);
    }

    private PatchRecruitApplicationStatusResDto changeRecruitApplicationState(RecruitApplication recruitApplication, Long memberId,
                                               MatchStatus status, RecruitApplicationValidator validator) {
        if(validator.hasError(recruitApplication, memberId)) {
            throw new RecruitException(RecruitErrorInfo.INVALID_CHANGE_MEMBER_OPERATION);
        }
        recruitApplication.changeStatus(status);
        return new PatchRecruitApplicationStatusResDto(recruitApplication.getId(), status.name());
    }

    private boolean isRecruitingType(String recruitType, Recruit recruit) {
        List<RecruitLimitation> recruitLimitations = recruit.getLimitations();

        for(RecruitLimitation recruitLimitation: recruitLimitations) {
            if(!recruitLimitation.getType().getName().equals(recruitType)) continue;
            if(recruitLimitation.getCurrentNumber() < recruitLimitation.getLimitation()) return true;
            throw new RecruitException(RecruitErrorInfo.IS_ALREADY_FULL);
        }
        return false;
    }

    private RecruitLimitation getNotFullRecruitLimitation(Recruit recruit, MetaData recruitType) {
        RecruitLimitation recruitLimitation = recruit.getLimitations()
                .stream()
                .filter(limit -> limit.getType().getName().equals(recruitType.getName()))
                .findFirst().orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        if(recruitLimitation.getCurrentNumber() >= recruitLimitation.getLimitation()) {
            throw new RecruitException(RecruitErrorInfo.IS_ALREADY_FULL);
        }
        return recruitLimitation;
    }

    private List<RecruitQuestionReply>  makeRecruitQuestionReplies(PostRecruitApplicationReqDto dto, Recruit recruit, RecruitApplication recruitApplication) {
        List<RecruitQuestionReply> replies = new ArrayList<>();
        List<RecruitQuestion> recruitQuestion = recruit.getQuestions();
        List<String> answers = dto.getContents();

        int len = recruitQuestion.size();
        if(answers.size() != len) {
            throw new RecruitException(RecruitErrorInfo.NOT_SAME_LENGTH_RECRUIT_QUESTION_ANSWER);
        }

        for(int i=0; i<len; ++i) {
            if(!StringUtils.hasText(dto.getContents().get(i))) {
                throw new RecruitException(RecruitErrorInfo.NOT_SAME_LENGTH_RECRUIT_QUESTION_ANSWER);
            }

            replies.add(
                    RecruitQuestionReply.builder()
                            .application(recruitApplication)
                            .question(recruitQuestion.get(i))
                            .content(dto.getContents().get(i))
                            .build()
            );
        }
        return replies;
    }
}
