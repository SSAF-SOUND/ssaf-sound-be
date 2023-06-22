package com.ssafy.ssafsound.batch.config;

import com.ssafy.ssafsound.batch.tasklet.PostTasklet;
import com.ssafy.ssafsound.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PostBatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PostService postService;


    @Bean
    public Job deleteHotPostJob() {
        return jobBuilderFactory
                .get("deleteHotPostJob")
                .start(deleteHotPostStep())
                .build();
    }

    @Bean
    public Step deleteHotPostStep() {
        return stepBuilderFactory.get("deleteHotPostJob")
                .tasklet(deleteHotPostTasklet())
                .build();
    }

    @Bean
    public Tasklet deleteHotPostTasklet() {
        return new PostTasklet(postService);
    }
}
