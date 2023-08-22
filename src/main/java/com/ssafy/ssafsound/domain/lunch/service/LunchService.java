package com.ssafy.ssafsound.domain.lunch.service;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.domain.LunchPoll;
import com.ssafy.ssafsound.domain.lunch.dto.*;
import com.ssafy.ssafsound.domain.lunch.exception.LunchErrorInfo;
import com.ssafy.ssafsound.domain.lunch.exception.LunchException;
import com.ssafy.ssafsound.domain.lunch.repository.LunchPollRepository;
import com.ssafy.ssafsound.domain.lunch.repository.LunchRepository;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LunchService {

    private final LunchRepository lunchRepository;

    private final LunchPollRepository lunchPollRepository;

    private final MemberRepository memberRepository;

    private final MetaDataConsumer metaDataConsumer;

    private final Clock clock;

    @Transactional(readOnly = true)
    public GetLunchListResDto findLunches(Long memberId, GetLunchListReqDto getLunchListReqDto) {

        LocalDate currentTime = LocalDate.now(clock);

        Integer dayDifference = getLunchListReqDto.getDate().compareTo(currentTime);
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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        LunchPoll lunchPoll = lunchPollRepository.findByMemberAndPolledAt(member, currentTime);
        Long polledAt = lunchPoll != null ? (long) lunches.indexOf(lunchPoll.getLunch()) : -1L;

        // 인증 유저 응답
        return GetLunchListResDto.of(lunches.stream()
                .map(lunch -> GetLunchListElementResDto.of(lunch, (long)lunch.getLunchPolls().size()))
                .collect(Collectors.toList()), polledAt);
    }
}
