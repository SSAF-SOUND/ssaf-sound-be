package com.ssafy.ssafsound.domain.lunch.service;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.domain.LunchPoll;
import com.ssafy.ssafsound.domain.lunch.dto.PostLunchPollResDto;
import com.ssafy.ssafsound.domain.lunch.exception.LunchErrorInfo;
import com.ssafy.ssafsound.domain.lunch.exception.LunchException;
import com.ssafy.ssafsound.domain.lunch.repository.LunchPollRepository;
import com.ssafy.ssafsound.domain.lunch.repository.LunchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LunchPollService {

    private final LunchRepository lunchRepository;

    private final LunchPollRepository lunchPollRepository;

    private final Clock clock;

    @Transactional
    public PostLunchPollResDto saveLunchPoll(Long memberId, Long lunchId) {

        LocalDate currentTime = LocalDate.now(clock);

        // 1. 점심 메뉴 존재 여부 확인
        Lunch lunch = lunchRepository.findById(lunchId)
                .orElseThrow(()-> new LunchException(LunchErrorInfo.INVALID_LUNCH_ID));

        // 2. 점심 메뉴가 당일 메뉴인지 확인
        if (!lunch.getCreatedAt().isEqual(currentTime)) {
            throw new LunchException(LunchErrorInfo.INVALID_DATE);
        }

        // 3. 오늘 투표한 점심 투표 엔티티 조회
        LunchPoll lunchPoll = lunchPollRepository.findByMember_IdAndPolledAt(memberId, currentTime);

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
        else lunchPollRepository.saveByMember_IdAndLunch_Id(memberId, lunchId, currentTime);

        return PostLunchPollResDto.of((long) lunch.getLunchPolls().size());
    }

    @Transactional
    public PostLunchPollResDto deleteLunchPoll(Long memberId, Long lunchId) {

        LocalDate currentTime = LocalDate.now(clock);

        // Lunch 엔티티 조회 후 validate
        Lunch lunch = lunchRepository.findById(lunchId)
                .orElseThrow(() -> new LunchException((LunchErrorInfo.INVALID_LUNCH_ID)));

        // 멤버 id와 Lunch 엔티티 기반으로 LunchPoll 엔티티 조회 후 validate
        LunchPoll lunchPoll = lunchPollRepository.findByMember_IdAndLunch(memberId, lunch)
                .orElseThrow(() -> new LunchException(LunchErrorInfo.NO_LUNCH_POLL));

        if (!lunchPoll.getPolledAt().isEqual(currentTime)) {
            throw new LunchException(LunchErrorInfo.INVALID_DATE);
        }

        // 투표 삭제
        lunchPollRepository.delete(lunchPoll);

        // 변화한 투표 수 리턴
        return PostLunchPollResDto.of((long) lunch.getLunchPolls().size());
    }
}
