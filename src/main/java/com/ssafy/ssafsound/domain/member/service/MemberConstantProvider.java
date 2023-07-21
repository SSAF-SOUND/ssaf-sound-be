package com.ssafy.ssafsound.domain.member.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.constant.certification")
@ConstructorBinding
public class MemberConstantProvider {
    private final Integer MAX_MINUTES;
    private final Integer CERTIFICATION_INQUIRY_TIME;
}
