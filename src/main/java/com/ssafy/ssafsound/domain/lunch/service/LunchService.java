package com.ssafy.ssafsound.domain.lunch.service;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.domain.LunchPoll;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListElementResDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListReqDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListResDto;
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

        // 조회 불가능한 일자 응답
        if (dayDifference < 0 || dayDifference > 1) {
            return GetLunchListResDto.ofEmpty();
        }

        MetaData campus = metaDataConsumer.getMetaData("CAMPUS", getLunchListReqDto.getCampus());

        List<Lunch> lunches = lunchRepository.findAllByCampusAndDate(campus, getLunchListReqDto.getDate())
                .orElse(null);

        if (lunches.isEmpty()) {
            return GetLunchListResDto.ofEmpty();
        }

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
                    .map(lunch -> GetLunchListElementResDto.of(lunch))
                    .collect(Collectors.toList()), -1);
        }

        // 투표한 점심 메뉴 찾기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        LunchPoll lunchPoll = lunchPollRepository.findByMemberAndPolledAt(member, currentTime);
        Integer polledAt = lunchPoll != null ? lunches.indexOf(lunchPoll.getLunch()) : -1;

        // 인증 유저 응답
        return GetLunchListResDto.of(lunches.stream()
                .map(GetLunchListElementResDto::of)
                .collect(Collectors.toList()), polledAt);
    }
}
