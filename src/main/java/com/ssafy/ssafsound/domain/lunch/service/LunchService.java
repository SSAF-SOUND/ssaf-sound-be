package com.ssafy.ssafsound.domain.lunch.service;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListElementResDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListReqDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchResDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListResDto;
import com.ssafy.ssafsound.domain.lunch.exception.LunchErrorInfo;
import com.ssafy.ssafsound.domain.lunch.exception.LunchException;
import com.ssafy.ssafsound.domain.lunch.repository.LunchPollRepository;
import com.ssafy.ssafsound.domain.lunch.repository.LunchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LunchService {

    private final LunchRepository lunchRepository;

    @Transactional(readOnly = true)
    public GetLunchListResDto findLunchesByCampusAndDate(GetLunchListReqDto getLunchListReqDto) {

        Integer dayDifference = getLunchListReqDto.getDate().compareTo(LocalDate.now());
        if (dayDifference < 0 || dayDifference > 1) throw new LunchException(LunchErrorInfo.INVALID_LUNCH_DATE);

        List<Lunch> lunches = lunchRepository.findAllByCampusIdAndDate(getLunchListReqDto.getCampusId(), getLunchListReqDto.getDate())
                .orElseThrow(()->new LunchException(LunchErrorInfo.NO_LUNCH_DATE));

        return GetLunchListResDto.of(lunches.stream()
                .map(lunch -> GetLunchListElementResDto.of(lunch, (long)lunch.getLunchPolls().size()))
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public GetLunchResDto findLunchByLunchId(Long lunchId) {

        return GetLunchResDto.of(lunchRepository.findById(lunchId)
                .orElseThrow(()->new LunchException(LunchErrorInfo.INVALID_LUNCH_ID)));
    }
}
