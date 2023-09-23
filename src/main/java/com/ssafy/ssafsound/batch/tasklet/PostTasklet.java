package com.ssafy.ssafsound.batch.tasklet;

import com.ssafy.ssafsound.domain.post.service.PostConstantProvider;
import com.ssafy.ssafsound.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
@RequiredArgsConstructor
public class PostTasklet implements Tasklet, StepExecutionListener {
    private final PostService postService;
    private final PostConstantProvider postConstantProvider;

    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        postService.deleteHotPostsUnderThreshold(postConstantProvider.getHOT_POST_LIKES_THRESHOLD());
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

}
