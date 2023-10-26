package com.ssafy.ssafsound.domain.term.service;

import com.ssafy.ssafsound.domain.term.domain.TermElement;
import com.ssafy.ssafsound.domain.term.dto.GetTermsResDto;
import com.ssafy.ssafsound.domain.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;

    @Transactional(readOnly = true)
    public GetTermsResDto getTerms() {
        return GetTermsResDto.fromTermElements(termRepository
                .getTerms().stream()
                .map(TermElement::from)
                .collect(Collectors.toList()));
    }
}
