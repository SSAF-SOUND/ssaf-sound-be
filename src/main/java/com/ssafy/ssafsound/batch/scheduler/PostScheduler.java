package com.ssafy.ssafsound.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableScheduling
@RequiredArgsConstructor
@Component
public class PostScheduler {
    private final Job deleteHotPostJob;
    private final JobLauncher jobLauncher;


    /**
     * 매일 오전 4시에 실행
     */
    @Scheduled(cron = "0 0 4 * * *")
    public void deleteHotPostScheduler() {
        try {
            Map<String, JobParameter> jobParametersMap = new HashMap<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            jobParametersMap.put("date", new JobParameter(dateFormat.format(new Date())));
            JobParameters parameters = new JobParameters(jobParametersMap);

            jobLauncher.run(deleteHotPostJob, parameters);

        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

}
