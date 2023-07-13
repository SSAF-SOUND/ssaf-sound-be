package com.ssafy.ssafsound.domain.member.dto;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostCertificationInfoResDto {
    
    private boolean possible;

    private Integer certificationInquiryCount;

    public static PostCertificationInfoResDto of(boolean possible, Integer certificationInquiryCount) {
        return PostCertificationInfoResDto.builder()
                .possible(possible)
                .certificationInquiryCount(certificationInquiryCount)
                .build();
    }
}
