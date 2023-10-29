package com.ssafy.ssafsound.domain.post.scheduler;

import com.ssafy.ssafsound.domain.post.service.PostConstantProvider;
import com.ssafy.ssafsound.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class PostScheduler {
    private final PostService postService;
    private final PostConstantProvider postConstantProvider;

    @Scheduled(cron = "0 0 4 * * *")
    private void deleteHotPostsUnderThreshold() {
        postService.deleteHotPostsUnderThreshold(postConstantProvider.getHOT_POST_LIKES_THRESHOLD());
    }
}
