package com.ssafy.ssafsound.domain.recruit.service;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.recruit.domain.*;
import com.ssafy.ssafsound.domain.recruit.dto.*;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitErrorInfo;
import com.ssafy.ssafsound.domain.recruit.exception.RecruitException;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitLimitationRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitScrapRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitSkillRepository;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import com.ssafy.ssafsound.domain.recruitapplication.domain.RecruitApplication;
import com.ssafy.ssafsound.domain.recruitapplication.repository.RecruitApplicationRepository;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final RecruitLimitationRepository recruitLimitationRepository;
    private final RecruitScrapRepository recruitScrapRepository;
    private final RecruitApplicationRepository recruitApplicationRepository;
    private final RecruitSkillRepository recruitSkillRepository;
    private final MemberRepository memberRepository;
    private final MetaDataConsumer metaDataConsumer;

    @Transactional
    public PostRecruitResDto saveRecruit(Long memberId, PostRecruitReqDto postRecruitReqDto) {
        Recruit recruit = postRecruitReqDto.to();
        Member register = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        // 등록자는 자신이 속한 역할군을 1가지 선택할 수 있어야한다.
        recruit.setRegister(register);
        recruit.setRegisterRecruitType(metaDataConsumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), postRecruitReqDto.getRegisterRecruitType()));

        List<RecruitSkill> skills = makeRecruitSkillFromPredefinedMetaData(metaDataConsumer, recruit, postRecruitReqDto.getSkills());
        recruit.setRecruitSkill(skills);
        recruitRepository.save(recruit);

        recruitLimitationRepository.saveAll(createRecruitLimitations(recruit, postRecruitReqDto.getLimitations()));
        return new PostRecruitResDto(recruit.getId());
    }

    @Transactional
    public PostRecruitScrapCountResDto toggleRecruitScrap(Long recruitId, Long memberId) {
        RecruitScrap recruitScrap = recruitScrapRepository.findByRecruitIdAndMemberId(recruitId, memberId)
                .orElse(null);

        boolean scraped = !isPreExistRecruitScrap(recruitId, memberId, recruitScrap);
        return new PostRecruitScrapCountResDto(recruitScrapRepository.countByRecruitId(recruitId), scraped);
    }

    @Transactional
    public GetRecruitDetailResDto getRecruitDetail(Long recruitId, Long memberId) {
        Recruit recruit = recruitRepository.findByIdUsingFetchJoinRegisterAndRecruitLimitation(recruitId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));
        long scrapCount = recruitScrapRepository.countByRecruitId(recruitId);
        Boolean scraped = recruitScrapRepository.existsByRecruitIdAndMemberId(recruitId, memberId);
        Boolean mine = recruit.getMember().getId().equals(memberId);

        RecruitApplication recruitApplication = recruitApplicationRepository.findTopByRecruitIdAndMemberIdOrderByIdDesc(recruitId, memberId);
        recruit.increaseView();
        return GetRecruitDetailResDto.of(recruit, scrapCount, scraped, mine, recruitApplication);
    }

    @Transactional
    public void updateRecruit(Long recruitId, Long memberId, PatchRecruitReqDto recruitReqDto) {
        Recruit recruit = recruitRepository.findByIdUsingFetchJoinRegisterAndRecruitLimitation(recruitId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));
        if(!recruit.getMember().getId().equals(memberId)) throw new RecruitException(RecruitErrorInfo.INVALID_CHANGE_MEMBER_OPERATION);

        // 리크루트 기술 스택 업데이트
        recruitSkillRepository.deleteAllByRecruit(recruit);
        List<RecruitSkill> recruitSkills = makeRecruitSkillFromPredefinedMetaData(metaDataConsumer, recruit, recruitReqDto.getSkills());
        if(recruitSkills != null) {
            recruit.setRecruitSkill(recruitSkills);
        }
        /*
            등록자 모집군 및 모집 인원 제한 수정
            1. 등록자의 모집군은 항상 변경가능하다.
            2. 모집인원 수정 시, 모집 완료된 인원이 있는 경우 해당 숫자 이하로 인원 제한을 수정할 수 없다.
            3. 모집군은 삭제할 수 있지만, 모집 완료된 인원이 있는 모집군의 경우 삭제할 수 없다.
            4. 모집군이 삭제되는 경우, 해당 타입에 존재하는 완료하지 않은 리크루팅 신청은 삭제되어야한다.
        */
        recruit.setRegisterRecruitType(metaDataConsumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), recruitReqDto.getRegisterRecruitType()));

        List<RecruitApplication> recruitApplications = recruitApplicationRepository.findByRecruitIdAndMatchStatus(recruitId, MatchStatus.DONE);
        List<RecruitLimitation> recruitLimitations = createRecruitLimitations(recruit, recruitReqDto.getLimitations());
        if(!increaseLimitationCurrentNumberByPreMatchDoneMember(recruitApplications, recruitLimitations)) {
            throw new RecruitException(RecruitErrorInfo.NOT_BELOW_PREV_LIMITATIONS);
        }

        recruitLimitationRepository.deleteAllByRecruit(recruit);

        recruit.update(recruitReqDto);
        recruit.setRecruitLimitations(recruitLimitations);
        recruitLimitationRepository.saveAll(recruitLimitations);

        List<MetaData> recruitTypes = recruitLimitations.stream()
                .map(RecruitLimitation::getType)
                .collect(Collectors.toList());

        recruitApplicationRepository.deleteByTypeNotIn(recruitTypes);
    }

    @Transactional
    public void deleteRecruit(Long recruitId, Long memberId) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));
        if(!recruit.getMember().getId().equals(memberId)) throw new RecruitException(RecruitErrorInfo.INVALID_CHANGE_MEMBER_OPERATION);
        recruit.delete();
    }

    @Transactional(readOnly = true)
    public GetRecruitsCursorResDto getRecruitsByCursor(GetRecruitsCursorReqDto getRecruitsCursorReqDto, Long loginMemberId) {
        Slice<Recruit> recruitPages = recruitRepository.findRecruitSliceByGetRecruitsReqDto(getRecruitsCursorReqDto, PageRequest.ofSize(getRecruitsCursorReqDto.getSize()));
        GetRecruitsCursorResDto recruitsResDto = GetRecruitsCursorResDto.fromPageAndMemberId(recruitPages, loginMemberId);
        if(!recruitsResDto.getRecruits().isEmpty()) {
            addRecruitParticipants(recruitsResDto);
        }
        return recruitsResDto;
    }

    @Transactional(readOnly = true)
    public GetRecruitOffsetResDto getRecruitsByOffset(GetRecruitsOffsetReqDto getRecruitsOffsetReqDto, Long loginMemberId) {
        Page<Recruit> recruitPages = recruitRepository.findRecruitPageByGetRecruitsReqDto(getRecruitsOffsetReqDto, getRecruitsOffsetReqDto.getPageRequest());
        GetRecruitOffsetResDto recruitsResDto = GetRecruitOffsetResDto.fromPageAndMemberId(recruitPages, loginMemberId);
        if(!recruitsResDto.getRecruits().isEmpty()) {
            addRecruitParticipants(recruitsResDto);
        }
        return recruitsResDto;
    }

    @Transactional(readOnly = true)
    public GetRecruitsCursorResDto getScrapedRecruitsByCursor(Long memberId, Long cursor, Pageable pageable) {
        Slice<Recruit> recruitPages = recruitRepository.findMemberScrapRecruitsByCursor(memberId, cursor, pageable);
        return GetRecruitsCursorResDto.fromPageAndMemberId(recruitPages, memberId);
    }

    @Transactional(readOnly = true)
    public GetRecruitsPageResDto getScrapedRecruitsByPage(Long memberId, RecruitOffsetPagingRequestDto pagingRequestDto) {
        Page<Recruit> recruitPages = recruitRepository.findMemberScrapRecruitsByPage(memberId, pagingRequestDto.getPageRequest());
        return GetRecruitsPageResDto.fromPageAndMemberId(recruitPages, memberId);
    }

    @Transactional
    public void expiredRecruit(Long recruitId, Long memberId) {
        Recruit recruit = recruitRepository.findByIdUsingFetchJoinRegister(recruitId)
                .orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        if(!recruit.getMember().getId().equals(memberId)) {
            throw new RecruitException(RecruitErrorInfo.INVALID_CHANGE_MEMBER_OPERATION);
        }
        recruit.expired();
    }

    @Transactional(readOnly = true)
    public GetRecruitsCursorResDto getMemberJoinRecruitsByCursor(GetMemberJoinRecruitsReqDto recruitsReqDto, Long loginMemberId) {
        Long memberId = recruitsReqDto.getMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        if(!memberId.equals(loginMemberId) && !member.getPublicProfile()) {
            throw new MemberException(MemberErrorInfo.MEMBER_PROFILE_SECRET);
        }

        Slice<Recruit> recruitPages = recruitRepository.findMemberJoinRecruitWithCursorAndPageable(memberId, recruitsReqDto.getCategory(), recruitsReqDto.getCursor(), PageRequest.ofSize(recruitsReqDto.getSize()));
        GetRecruitsCursorResDto recruitsResDto = GetRecruitsCursorResDto.fromPageAndMemberId(recruitPages, loginMemberId);
        if(!recruitsResDto.getRecruits().isEmpty()) {
            addRecruitParticipants(recruitsResDto);
        }
        return recruitsResDto;
    }

    @Transactional(readOnly = true)
    public GetRecruitsPageResDto getMemberJoinRecruitsByPage(GetMemberJoinOffsetRecruitReqDto recruitsReqDto, Long loginMemberId) {
        Long memberId = recruitsReqDto.getMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        if(!memberId.equals(loginMemberId) && !member.getPublicProfile()) {
            throw new MemberException(MemberErrorInfo.MEMBER_PROFILE_SECRET);
        }

        Page<Recruit> recruitPages = recruitRepository.findMemberJoinRecruitWithPageable(memberId, recruitsReqDto.getCategory(), recruitsReqDto.getPageRequest());

        GetRecruitsPageResDto recruitsResDto = GetRecruitsPageResDto.fromPageAndMemberId(recruitPages, loginMemberId);
        if(!recruitsResDto.getRecruits().isEmpty()) {
            addRecruitParticipants(recruitsResDto);
        }
        return recruitsResDto;
    }

    @Transactional
    public GetMemberAppliedRecruitsCursorResDto getMemberAppliedRecruitsCursor(GetMemberAppliedRecruitsCursorReqDto recruitsReqDto, Long memberId) {
        memberRepository.findById(memberId).orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        Slice<AppliedRecruit> appliedRecruitSlice = recruitRepository.findMemberAppliedRecruitsByCursor(memberId,
                recruitsReqDto.getCursor(),
                recruitsReqDto.getCategory(),
                recruitsReqDto.getMatchStatus(),
                Pageable.ofSize(recruitsReqDto.getSize()));

        GetMemberAppliedRecruitsCursorResDto recruitsResDto = GetMemberAppliedRecruitsCursorResDto.fromPageAndMemberId(appliedRecruitSlice, memberId);
        if(!recruitsResDto.getRecruits().isEmpty()) {
            addRecruitParticipants(recruitsResDto);
        }
        return recruitsResDto;
    }

    @Transactional
    public GetMemberAppliedRecruitsOffsetResDto getMemberAppliedRecruitsByOffset(GetMemberAppliedRecruitOffsetReqDto recruitsReqDto, Long memberId) {
        memberRepository.findById(memberId).orElseThrow(()->new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        Page<AppliedRecruit> appliedRecruitSlice = recruitRepository.findMemberAppliedRecruitsByPage(memberId,
                recruitsReqDto.getCategory(),
                recruitsReqDto.getMatchStatus(),
                recruitsReqDto.getPageRequest());

        GetMemberAppliedRecruitsOffsetResDto recruitsResDto = GetMemberAppliedRecruitsOffsetResDto.fromPageAndMemberId(appliedRecruitSlice, memberId);
        if(!recruitsResDto.getRecruits().isEmpty()) {
            addRecruitParticipants(recruitsResDto);
        }
        return recruitsResDto;
    }

    private void addRecruitParticipants(AddParticipantDto recruitsResDto) {
        // Recruit Id, Recruit Type 두 depth로 이루어진 Map으로 리크루트별 참여자 목록을 가공한다.
        Map<Long, Map<String, RecruitParticipant>> participantsMap = recruitsResDto.getRecruitParticipantMapByRecruitIdAndRecruitType();

        // 검색 결과에 존재하는 RecruitId를 이용, 참여 확정된 참여자 정보를 조회한다.
        List<Long> recruitIds = recruitsResDto.getRecruitsId();
        List<RecruitApplication> recruitApplications = recruitApplicationRepository
                .findDoneRecruitApplicationByRecruitIdInFetchRecruitAndMember(recruitIds);

        // 참여자 정보를 참여자 목록에 추가한 후 결과를 사용자에게 리턴한다.
        for(RecruitApplication recruitApplication: recruitApplications) {
            Long recruitId = recruitApplication.getRecruit().getId();
            String recruitType = recruitApplication.getType().getName();

            RecruitParticipant participant = participantsMap.get(recruitId).get(recruitType);
            Member member = recruitApplication.getMember();
            participant.addParticipant(member.getNickname(), member.getMajor());
        }
    }

    private boolean increaseLimitationCurrentNumberByPreMatchDoneMember(List<RecruitApplication> recruitApplications, List<RecruitLimitation> recruitLimitations) {
        Map<String, Integer> prevMatchDoneApplicationCounterMap = new HashMap<>();
        for(RecruitApplication recruitApplication: recruitApplications) {
            String recruitType = recruitApplication.getType().getName();
            Integer existCount = prevMatchDoneApplicationCounterMap.getOrDefault(recruitType, 0);
            prevMatchDoneApplicationCounterMap.put(recruitType, existCount+1);
        }

        for(RecruitLimitation recruitLimitation: recruitLimitations) {
            String recruitType = recruitLimitation.getType().getName();
            Integer count = prevMatchDoneApplicationCounterMap.get(recruitType);
            if(count == null) {
                continue;
            }
            if(count > recruitLimitation.getLimitation()) {
                return false;
            }

            recruitLimitation.setCurrentNumber(count);
            prevMatchDoneApplicationCounterMap.remove(recruitType);
        }
        return prevMatchDoneApplicationCounterMap.isEmpty();
    }

    private List<RecruitSkill> makeRecruitSkillFromPredefinedMetaData(MetaDataConsumer consumer, Recruit recruit, List<String> skills) {
        if(skills == null) return null;
        return skills.stream().map((skill)->(
                RecruitSkill.builder()
                        .recruit(recruit)
                        .skill(consumer.getMetaData(MetaDataType.SKILL.name(), skill)))
                .build()
        ).collect(Collectors.toList());
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
