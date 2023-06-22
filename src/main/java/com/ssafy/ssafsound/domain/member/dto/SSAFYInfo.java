package com.ssafy.ssafsound.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SSAFYInfo {

    private Integer year;
    private boolean isMajor;
    private String campus;
    private String certificationState;
}
