package com.ssafy.ssafsound.domain.lunch.service;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.domain.LunchPoll;
import com.ssafy.ssafsound.domain.lunch.dto.*;
import com.ssafy.ssafsound.domain.lunch.exception.LunchErrorInfo;
import com.ssafy.ssafsound.domain.lunch.exception.LunchException;
import com.ssafy.ssafsound.domain.lunch.repository.LunchPollRepository;
import com.ssafy.ssafsound.domain.lunch.repository.LunchRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LunchService {

    private final LunchRepository lunchRepository;

    private final LunchPollRepository lunchPollRepository;

    @Qualifier("EnumMetaDataConsumer")
    private final MetaDataConsumer metaDataConsumer;

    @Transactional(readOnly = true)
    public GetLunchListResDto findLunches(Long memberId, GetLunchListReqDto getLunchListReqDto) {

        Integer dayDifference = getLunchListReqDto.getDate().compareTo(LocalDate.now());
        if (dayDifference < 0 || dayDifference > 1) throw new LunchException(LunchErrorInfo.INVALID_DATE);

        MetaData campus = metaDataConsumer.getMetaData("CAMPUS",getLunchListReqDto.getCampus());

        List<Lunch> lunches = lunchRepository.findAllByCampusAndDate(campus, getLunchListReqDto.getDate())
                .orElseThrow(()->new LunchException(LunchErrorInfo.NO_LUNCH_DATE));

        // 투표수 내림차순 정렬
        lunches.sort((a, b) -> {
            if (a.getLunchPolls().size() == b.getLunchPolls().size()) {
                return (int) (a.getId() - b.getId());
            }
            return b.getLunchPolls().size() - a.getLunchPolls().size();
        });

        // 미인증 유저 응답
        if (memberId == null) {
            return GetLunchListResDto.of(lunches.stream()
                    .map(lunch -> GetLunchListElementResDto.of(lunch, (long)lunch.getLunchPolls().size()))
                    .collect(Collectors.toList()), -1L);
        }

        // 투표한 점심 메뉴 찾기
        Lunch polledLunch = lunchPollRepository.findByMember_IdAndPolledAt(memberId, LocalDate.now()).getLunch();

        // 인증 유저 응답
        return GetLunchListResDto.of(lunches.stream()
                .map(lunch -> GetLunchListElementResDto.of(lunch, (long)lunch.getLunchPolls().size()))
                .collect(Collectors.toList()), (long) lunches.indexOf(polledLunch));
    }

    @Transactional(readOnly = true)
    public GetLunchResDto findLunchDetail(Long lunchId) {

        return GetLunchResDto.of(lunchRepository.findById(lunchId)
                .orElseThrow(()->new LunchException(LunchErrorInfo.INVALID_LUNCH_ID)));
    }

    @Transactional
    public PostLunchPollResDto saveLunchPoll(Long memberId, Long lunchId) {

        // 1. 점심 메뉴 존재 여부 확인
        Lunch lunch = lunchRepository.findById(lunchId)
                .orElseThrow(()-> new LunchException(LunchErrorInfo.INVALID_LUNCH_ID));

        // 2. 점심 메뉴가 당일 메뉴인지 확인
        if (!lunch.getCreatedAt().isEqual(LocalDate.now())) {
            throw new LunchException(LunchErrorInfo.INVALID_DATE);
        }

        // 3. 오늘 투표한 점심 투표 엔티티 조회
        LunchPoll lunchPoll = lunchPollRepository.findByMember_IdAndPolledAt(memberId, LocalDate.now());

        // 4-1. 이미 오늘 투표한 경우
        if (lunchPoll != null) {
            // 5-1. 중복 투표인 경우
            if (lunchPoll.getLunch().equals(lunch)) {
                throw new LunchException(LunchErrorInfo.DUPLICATE_LUNCH_POLL);
            }

            // 5-2. 투표 선택지가 바뀐 경우
            lunchPoll.setLunch(lunch);
        }

        // 4-2. 오늘 첫 투표인 경우
        else lunchPollRepository.saveByMember_IdAndLunch_Id(memberId, lunchId, LocalDate.now());

        return PostLunchPollResDto.of((long) lunch.getLunchPolls().size());
    }

    @Transactional
    public PostLunchPollResDto deleteLunchPoll(Long memberId, Long lunchId) {

        // Lunch 엔티티 조회 후 validate
        Lunch lunch = lunchRepository.findById(lunchId)
                .orElseThrow(() -> new LunchException((LunchErrorInfo.INVALID_LUNCH_ID)));

        // 멤버 id와 Lunch 엔티티 기반으로 LunchPoll 엔티티 조회 후 validate
        LunchPoll lunchPoll = lunchPollRepository.findByMember_IdAndLunch(memberId, lunch)
                .orElseThrow(() -> new LunchException(LunchErrorInfo.NO_LUNCH_POLL));

        // 투표 삭제
        lunchPollRepository.delete(lunchPoll);

        // 변화한 투표 수 리턴
        return PostLunchPollResDto.of((long) lunch.getLunchPolls().size());
    }

}
