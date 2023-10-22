package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.term.domain.Term;
import com.ssafy.ssafsound.domain.term.domain.TermElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TermFixture {

    public List<Term> createTerms(Set<Long> termIds) {
        List<Term> terms = new ArrayList<>();

        for (Long termId : termIds) {
            terms.add(Term.builder()
                    .id(termId)
                    .name("이용 약관" + termId)
                    .used(true)
                    .required(true)
                    .sequence(termId.intValue())
                    .content("test" + termId)
                    .build());
        }

        return terms;
    }

    public List<TermElement> createTermElements() {
        Set<Long> termIds = new HashSet<>(Set.of(1L, 2L, 3L));
        List<Term> terms = this.createTerms(termIds);
        return terms.stream()
                .map(TermElement::from)
                .collect(Collectors.toList());
    }

    public List<TermElement> createTermElements(List<Term> terms) {
        return terms.stream()
                .map(TermElement::from)
                .collect(Collectors.toList());
    }
}
