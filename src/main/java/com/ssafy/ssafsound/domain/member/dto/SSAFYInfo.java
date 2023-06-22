package com.ssafy.ssafsound.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SSAFYInfo {

    private Integer semester;
    private boolean isMajor;
    private String campus;
    private String certificationState;
    private String majorType;
}
