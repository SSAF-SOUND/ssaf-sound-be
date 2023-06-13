package com.ssafy.ssafsound.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EnvelopeResponse<T> {

    @Builder.Default
    private String code = "200";

    @Builder.Default
    private String message = "success";

    private T data;
}
