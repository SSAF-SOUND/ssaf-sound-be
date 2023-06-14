package com.ssafy.ssafsound.domain.lunch.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LunchException extends RuntimeException{

    private LunchErrorInfo info;
}
