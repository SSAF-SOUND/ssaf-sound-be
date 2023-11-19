package com.ssafy.ssafsound.domain.lunch.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.exception.AuthErrorInfo;
import com.ssafy.ssafsound.domain.auth.exception.AuthException;
import com.ssafy.ssafsound.domain.lunch.dto.GetLunchListReqDto;
import com.ssafy.ssafsound.domain.lunch.dto.GetScrapReqDto;
import com.ssafy.ssafsound.domain.lunch.exception.LunchErrorInfo;
import com.ssafy.ssafsound.domain.lunch.exception.LunchException;
import com.ssafy.ssafsound.domain.lunch.repository.LunchRepository;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.meta.domain.Campus;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LunchScrapService {

    private final LunchRepository lunchRepository;
    private final MetaDataConsumer metaDataConsumer;
    private final ScrapInfoProviderFactory scrapInfoProviderFactory;

    @Transactional(readOnly = false)
    public void scrapLunchManually(AuthenticatedMember authenticatedMember, GetLunchListReqDto getLunchListReqDto) {

        String memberRole = Objects.requireNonNull(authenticatedMember.getMemberRole(), MemberErrorInfo.MEMBER_ROLE_TYPE_NOT_FOUND.getMessage());

        if (!memberRole.equals("admin")) {
            throw new AuthException(AuthErrorInfo.UNAUTHORIZED_ERROR);
        }

        MetaData campus = metaDataConsumer.getMetaData("CAMPUS", getLunchListReqDto.getCampus());

        if (getLunchListReqDto.getCampus().equals(Campus.DAEJEON.getName())) {
            throw new LunchException(LunchErrorInfo.SCRAPING_ERROR);
        }

        if (getLunchListReqDto.getCampus().equals(Campus.GWANGJU.getName())) {
            scrapFreshmealLunch(campus);
        } else {
            scrapWelstoryLunch(getLunchListReqDto.getDate(), campus);
        }
    }

    @Transactional(readOnly = false)
    public void scrapWelstoryLunch(LocalDate date, MetaData... campuses) {

        ScrapInfoProvider scraper = scrapInfoProviderFactory.getProviderFrom("welstory");

        List<GetScrapReqDto> requests = Arrays.stream(campuses)
                .map(campus -> GetScrapReqDto.builder()
                        .campus(campus)
                        .menuDt(date)
                        .build())
                .collect(Collectors.toList());

        lunchRepository.saveAll(scraper.scrapLunchInfo(requests));
    }

    @Transactional(readOnly = false)
    public void scrapFreshmealLunch(MetaData... campuses) {

        ScrapInfoProvider scraper = scrapInfoProviderFactory.getProviderFrom("freshmeal");

        List<GetScrapReqDto> requests = Arrays.stream(campuses)
                .map(campus -> GetScrapReqDto.builder()
                        .campus(campus)
                        .build())
                .collect(Collectors.toList());

        lunchRepository.saveAll(scraper.scrapLunchInfo(requests));
    }
}
