package com.ssafy.ssafsound.global.config;

import com.ssafy.ssafsound.domain.member.service.MemberConstantProvider;
import com.ssafy.ssafsound.domain.member.service.SemesterConstantProvider;
import com.ssafy.ssafsound.domain.post.service.PostConstantProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({MemberConstantProvider.class, SemesterConstantProvider.class, PostConstantProvider.class})
@RequiredArgsConstructor
public class MagicNumberConfig {
}
