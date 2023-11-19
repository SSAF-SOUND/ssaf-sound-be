package com.ssafy.ssafsound.domain.lunch.service;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.domain.LunchPoll;
import com.ssafy.ssafsound.domain.lunch.dto.PostLunchPollResDto;
import com.ssafy.ssafsound.domain.lunch.exception.LunchErrorInfo;
import com.ssafy.ssafsound.domain.lunch.exception.LunchException;
import com.ssafy.ssafsound.domain.lunch.repository.LunchPollRepository;
import com.ssafy.ssafsound.domain.lunch.repository.LunchRepository;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
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

    private final MemberRepository memberRepository;

    private final Clock clock;

    @Transactional
    public PostLunchPollResDto saveLunchPoll(Long memberId, Long lunchId) {

        LocalDate currentTime = LocalDate.now(clock);

        // 1. 점심 메뉴 존재 여부 확인
        Lunch lunch = lunchRepository.findById(lunchId)
                .orElseThrow(()-> new LunchException(LunchErrorInfo.INVALID_LUNCH_ID));

        // Member 엔티티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        // 2. 점심 메뉴가 당일 메뉴인지 확인
        if (!lunch.getCreatedAt().isEqual(currentTime)) {
            throw new LunchException(LunchErrorInfo.INVALID_DATE);
        }

        // 3. 같은 캠퍼스에 오늘 투표한 점심 투표 엔티티 조회
        LunchPoll lunchPoll = lunchPollRepository.findByMemberAndCampusAndPolledAt(member, lunch.getCampus(), currentTime);

        // 4-1. 이미 오늘 투표한 경우
        if (lunchPoll != null) {
            // 5-1. 중복 투표인 경우
            if (lunchPoll.getLunch().equals(lunch)) {
                throw new LunchException(LunchErrorInfo.DUPLICATE_LUNCH_POLL);
            }

            // 5-2. 투표 선택지가 바뀐 경우
            lunchPoll.setLunch(lunch);

        } else {
            // 4-2. 오늘 첫 투표인 경우
            lunchPoll = LunchPoll.builder()
                    .member(member)
                    .polledAt(currentTime)
                    .build();

            lunchPoll.setLunch(lunch);

            lunchPollRepository.save(lunchPoll);
        }

        return PostLunchPollResDto.of(lunch.getPollCount());
    }

    @Transactional
    public PostLunchPollResDto deleteLunchPoll(Long memberId, Long lunchId) {

        LocalDate currentTime = LocalDate.now(clock);

        // Lunch 엔티티 조회 후 validate
        Lunch lunch = lunchRepository.findById(lunchId)
                .orElseThrow(() -> new LunchException((LunchErrorInfo.INVALID_LUNCH_ID)));

        if (!lunch.getCreatedAt().isEqual(currentTime)) {
            throw new LunchException(LunchErrorInfo.INVALID_DATE);
        }

        // Member 엔티티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        // 멤버 id와 Lunch 엔티티 기반으로 LunchPoll 엔티티 조회 후 validate
        LunchPoll lunchPoll = lunchPollRepository.findByMemberAndLunch(member, lunch)
                .orElseThrow(() -> new LunchException(LunchErrorInfo.NO_LUNCH_POLL));

        // 투표 삭제
        lunchPoll.deleteLunchPoll();
        lunchPollRepository.delete(lunchPoll);

        // 변화한 투표 수 리턴
        return PostLunchPollResDto.of(lunch.getPollCount());
    }
}
