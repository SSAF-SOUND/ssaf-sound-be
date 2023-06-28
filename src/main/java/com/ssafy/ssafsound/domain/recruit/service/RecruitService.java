package com.ssafy.ssafsound.domain.recruit.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.recruit.domain.*;
import com.ssafy.ssafsound.domain.recruit.dto.GetRecruitDetailResDto;
import com.ssafy.ssafsound.domain.recruit.dto.PatchRecruitReqDto;
import com.ssafy.ssafsound.domain.recruit.dto.PostRecruitReqDto;
import com.ssafy.ssafsound.domain.recruit.dto.RecruitLimitElement;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
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
    private final RecruitScrapRepository recruitScrapRepository;
    private final MemberRepository memberRepository;
    private final MetaDataConsumer metaDataConsumer;

    @Transactional
    public Recruit saveRecruit(Long memberId, PostRecruitReqDto postRecruitReqDto) {
        Recruit recruit = postRecruitReqDto.to();
        Member register = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        // 등록자는 자신이 속한 역할군을 1가지 선택할 수 있어야한다.
        recruit.setRegister(register);
        recruit.setRegisterRecruitType(metaDataConsumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), postRecruitReqDto.getRegisterRecruitType()));

        setRecruitSkillFromPredefinedMetaData(metaDataConsumer, recruit, postRecruitReqDto.getSkills());
        recruitRepository.save(recruit);

        recruitLimitationRepository.saveAll(createRecruitLimitations(recruit, postRecruitReqDto.getLimitations()));
        return recruit;
    }

    @Transactional
    public boolean toggleRecruitScrap(Long recruitId, Long memberId) {
        RecruitScrap recruitScrap = recruitScrapRepository.findByRecruitIdAndMemberId(recruitId, memberId)
                .orElse(null);
        return isPreExistRecruitScrap(recruitId, memberId, recruitScrap);
    }

    @Transactional
    public GetRecruitDetailResDto getRecruitDetail(Long recruitId) {
        Recruit recruit = recruitRepository.findByIdUsingFetchJoinRegisterAndRecruitLimitation(recruitId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));
        recruit.increaseView();
        return GetRecruitDetailResDto.from(recruit);
    }

    @Transactional
    public void updateRecruit(Long recruitId, Long memberId, PatchRecruitReqDto recruitReqDto) {
        Recruit recruit = recruitRepository.findByIdUsingFetchJoinRegisterAndRecruitLimitation(recruitId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));
        if(!recruit.getMember().getId().equals(memberId)) throw new RecruitException(RecruitErrorInfo.INVALID_CHANGE_MEMBER_OPERATION);

        setRecruitSkillFromPredefinedMetaData(metaDataConsumer, recruit, recruitReqDto.getSkills());
        updateRecruitLimitations(recruitReqDto, recruit);
        recruit.update(recruitReqDto);
    }

    @Transactional
    public void deleteRecruit(Long recruitId, Long memberId) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));
        if(!recruit.getMember().getId().equals(memberId)) throw new RecruitException(RecruitErrorInfo.INVALID_CHANGE_MEMBER_OPERATION);
        recruit.delete();
    }

    private void updateRecruitLimitations(PatchRecruitReqDto recruitReqDto, Recruit recruit) {
        List<RecruitLimitation> prevLimitations = recruit.getLimitations();
        List<RecruitLimitation> updateLimitations = createRecruitLimitations(recruit, recruitReqDto.getLimitations());

        for(RecruitLimitation prevLimitation: prevLimitations) {
            RecruitLimitation updateInfo = updateLimitations.stream().filter(updateLimit ->
                    updateLimit.getType().getName()
                            .equals(prevLimitation.getType().getName()))
                    .findAny().orElseThrow(()->new RecruitException(RecruitErrorInfo.NOT_BELOW_PREV_LIMITATIONS));

            if(updateInfo.getLimitation() < prevLimitation.getLimitation()) {
                throw new RecruitException(RecruitErrorInfo.NOT_BELOW_PREV_LIMITATIONS);
            }
        }
        recruit.setRecruitLimitations(updateLimitations);
    }

    private void setRecruitSkillFromPredefinedMetaData(MetaDataConsumer consumer, Recruit recruit, List<String> skills) {
        if(skills == null) return;
        List<RecruitSkill> recruitSkills = skills.stream().map((skill)->(
                RecruitSkill.builder()
                        .recruit(recruit)
                        .skill(consumer.getMetaData(MetaDataType.SKILL.name(), skill)))
                .build()
        ).collect(Collectors.toList());
        recruit.setRecruitSkill(recruitSkills);
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

    private List<RecruitLimitation> createRecruitLimitations(Recruit recruit, List<RecruitLimitElement> limits) {
        List<RecruitLimitation> limitations =  limits.stream().map((limit)->
                RecruitLimitation.builder()
                        .recruit(recruit)
                        .type(metaDataConsumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), limit.getRecruitType()))
                        .limitation(limit.getLimit())
                        .currentNumber(0)
                        .build())
                .collect(Collectors.toList());

        recruit.setRecruitLimitations(limitations);
        return limitations;
    }
}
