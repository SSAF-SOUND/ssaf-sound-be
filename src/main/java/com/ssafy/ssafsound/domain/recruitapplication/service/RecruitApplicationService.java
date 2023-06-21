package com.ssafy.ssafsound.domain.recruitapplication.service;

import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitLimitation;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitQuestion;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitQuestionReply;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitQuestionReplyRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import com.ssafy.ssafsound.domain.recruitapplication.dto.PostRecruitApplicationReqDto;
import com.ssafy.ssafsound.domain.recruitapplication.repository.RecruitApplicationRepository;
import com.ssafy.ssafsound.domain.recruitapplication.validator.RecruitApplicationValidator;
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
    private final MemberRepository memberRepository;
    private final MetaDataConsumer metaDataConsumer;

    @Transactional
    public void saveRecruitApplication(Long recruitId, Long memberId, PostRecruitApplicationReqDto postRecruitApplicationReqDto) {
        Recruit recruit = recruitRepository.findByIdUsingFetchJoinRecruitLimitation(recruitId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));
        String recruitType = postRecruitApplicationReqDto.getRecruitType();
        if(!isRecruitingType(recruitType, recruit)) {
            throw new RecruitException(RecruitErrorInfo.NOT_RECRUITING_TYPE);
        }

        RecruitApplication recruitApplication = postRecruitApplicationReqDto.createRecruitApplicationFromPredefinedMetadata(
                memberRepository.getReferenceById(memberId),
                recruit,
                metaDataConsumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), recruitType));
        List<RecruitQuestionReply> participantAnswers = makeRecruitQuestionReplies(postRecruitApplicationReqDto, recruit, recruitApplication);
        recruitApplicationRepository.save(recruitApplication);
        recruitQuestionReplyRepository.saveAll(participantAnswers);
    }

    @Transactional
    public void approveRecruitApplicationByRegister(Long recruitApplicationId, Long memberId, MatchStatus status) {
        RecruitApplication recruitApplication = recruitApplicationRepository.findByIdFetchRecruitWriter(recruitApplicationId)
                .orElseThrow(() -> new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        getNotFullRecruitLimitation(recruitApplication.getRecruit(), recruitApplication.getType());
        changeRecruitApplicationState(recruitApplication, memberId, status,
                (entity, mid) -> {
                    boolean isNotRegister = !entity.getRecruit().getMember().getId().equals(mid);
                    boolean isNotValidMatchStatus = !entity.getMatchStatus().equals(MatchStatus.WAITING_REGISTER_APPROVE);
                    return isNotRegister || isNotValidMatchStatus;
                });
    }

    @Transactional
    public void joinRecruitApplication(Long recruitApplicationId, Long memberId, MatchStatus status) {
        RecruitApplication recruitApplication = recruitApplicationRepository.findByIdAndMemberId(recruitApplicationId, memberId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        RecruitLimitation recruitLimitation = getNotFullRecruitLimitation(recruitApplication.getRecruit(), recruitApplication.getType());
        recruitLimitation.increaseCurrentNumber();
        changeRecruitApplicationState(recruitApplication, memberId, status,
                (entity, mid)-> !entity.getMatchStatus().equals(MatchStatus.WAITING_APPLICANT));
    }

    @Transactional
    public void rejectRecruitApplication(Long recruitApplicationId, Long memberId, MatchStatus status) {
        RecruitApplication recruitApplication = recruitApplicationRepository.findByIdAndMemberIdFetchRecruitWriter(recruitApplicationId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        changeRecruitApplicationState(recruitApplication, memberId, status,
                (entity, mid)-> {
                    boolean isNotValidRegisterAndState = (!entity.getRecruit().getMember().getId().equals(mid) || !entity.getMatchStatus().equals(MatchStatus.WAITING_REGISTER_APPROVE));
                    boolean isNotValidParticipantAndStatus = (!entity.getMember().getId().equals(mid) || !entity.getMatchStatus().equals(MatchStatus.WAITING_APPLICANT));
                    return isNotValidRegisterAndState && isNotValidParticipantAndStatus;
                });
    }

    @Transactional
    public void cancelRecruitApplicationByParticipant(Long recruitApplicationId, Long memberId, MatchStatus status) {
        RecruitApplication recruitApplication = recruitApplicationRepository.findByIdAndMemberId(recruitApplicationId, memberId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        changeRecruitApplicationState(recruitApplication, memberId, status,
                (entity, mid)-> recruitApplication.getMatchStatus() != MatchStatus.WAITING_REGISTER_APPROVE
        );
    }

    private void changeRecruitApplicationState(RecruitApplication recruitApplication, Long memberId,
                                               MatchStatus status, RecruitApplicationValidator validator) {
        if(validator.hasError(recruitApplication, memberId)) {
            throw new RecruitException(RecruitErrorInfo.INVALID_CHANGE_MEMBER_OPERATION);
        }
        recruitApplication.changeStatus(status);
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
