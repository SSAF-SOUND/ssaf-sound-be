package com.ssafy.ssafsound.domain.term.dto;

import com.ssafy.ssafsound.domain.term.domain.TermElement;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetTermsResDto {
    List<TermElement> termElements;

    public static GetTermsResDto fromTermElements(List<TermElement> termElements) {
        return GetTermsResDto.builder()
                .termElements(termElements)
                .build();
    }
}
