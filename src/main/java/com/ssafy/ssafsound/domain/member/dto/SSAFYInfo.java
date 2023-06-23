package com.ssafy.ssafsound.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SSAFYInfo {

    private Integer semester;
    private boolean isMajor;
    private String campus;
    @Builder.Default
    private String certificationState = null;
    @Builder.Default
    private String majorType = null;
}
