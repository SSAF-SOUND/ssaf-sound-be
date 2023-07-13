package com.ssafy.ssafsound.domain.recruit.task;

import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecruitCron {
    private final RecruitRepository recruitRepository;

    // 매일 00:00시 모집기간이 끝난 리크루트를 완료시킨다.
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void expiredRecruits() {
        log.info("Batch job start: set the value of an expired recruit");
        int effectRows = recruitRepository.expiredTimeOutRecruits(LocalDate.now().atTime(LocalTime.MAX));
        log.info("{} recruits expired", effectRows);
    }
}
