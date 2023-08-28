package com.ssafy.ssafsound.domain.member.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.constant.semester")
@ConstructorBinding
public class SemesterConstantProvider {

    private final Integer MIN_SEMESTER;

    private final Integer MAX_SEMESTER;
}
