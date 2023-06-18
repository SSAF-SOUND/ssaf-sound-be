package com.ssafy.ssafsound.domain.recruit.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.recruit.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitApplication;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitLimitation;
import com.ssafy.ssafsound.domain.recruit.dto.PostRecruitReqDto;
import com.ssafy.ssafsound.domain.recruit.dto.RecruitLimitElement;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitApplicationRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitLimitationRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
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
