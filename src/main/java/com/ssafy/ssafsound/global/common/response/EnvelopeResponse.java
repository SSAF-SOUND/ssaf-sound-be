package com.ssafy.ssafsound.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvelopeResponse<T> {
    private String code;
    private String message;
    private T data;
}
