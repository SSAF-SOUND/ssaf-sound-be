package com.ssafy.ssafsound.domain.recruit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecruitException extends RuntimeException {

    private RecruitErrorInfo info;
}
