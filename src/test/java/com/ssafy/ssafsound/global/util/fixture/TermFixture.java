package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.term.domain.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TermFixture {

    public List<Term> createTerms(Set<Long> termIds) {
        List<Term> terms = new ArrayList<>();

        for (Long termId : termIds) {
            terms.add(Term.builder()
                    .id(termId)
                    .name("이용 약관" + termId)
                    .usedYN(true)
                    .requiredYN(true)
                    .sequence(1)
                    .content("test" + termId)
                    .build());
        }

        return terms;
    }
}
