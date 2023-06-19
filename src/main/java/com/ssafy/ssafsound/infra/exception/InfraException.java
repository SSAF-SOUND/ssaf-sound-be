package com.ssafy.ssafsound.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InfraException extends RuntimeException{

    private InfraErrorInfo info;
}
