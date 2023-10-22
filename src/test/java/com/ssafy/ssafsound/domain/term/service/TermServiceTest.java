package com.ssafy.ssafsound.domain.term.service;

import com.ssafy.ssafsound.domain.term.domain.Term;
import com.ssafy.ssafsound.domain.term.domain.TermElement;
import com.ssafy.ssafsound.domain.term.dto.GetTermsResDto;
import com.ssafy.ssafsound.domain.term.repository.TermRepository;
import com.ssafy.ssafsound.global.util.fixture.TermFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TermServiceTest {

    @Mock
    private TermRepository termRepository;

    @InjectMocks
    private TermService termService;

    private TermFixture termFixture = new TermFixture();

    @DisplayName("서비스에서 사용중인 약관 목록 조회에 성공한다.")
    @Test
    void Given_UsedTerms_When_RequestTerms_Then_Success() {
        //given
        Set<Long> termIds = new HashSet<>(Set.of(1L, 2L, 3L));
        List<Term> terms = termFixture.createTerms(termIds);
        given(termRepository.getTerms()).willReturn(terms);
        List<TermElement> expect = termFixture.createTermElements(terms);

        //when
        GetTermsResDto getTermsResDto = termService.getTerms();

        //then
        assertThat(getTermsResDto.getTermElements())
                .usingRecursiveComparison()
                .isEqualTo(expect);

        //verify
        verify(termRepository, times(1)).getTerms();
    }
}