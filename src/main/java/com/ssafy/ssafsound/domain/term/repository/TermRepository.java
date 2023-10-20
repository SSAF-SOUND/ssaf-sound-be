package com.ssafy.ssafsound.domain.term.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafsound.domain.term.domain.Term;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ssafy.ssafsound.domain.term.domain.QTerm.term;

@Repository
@RequiredArgsConstructor
public class TermRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Term> getTerms() {
        return jpaQueryFactory
                .selectFrom(term)
                .where(term.used.eq(true))
                .orderBy(term.sequence.asc())
                .fetch();

    }

    public List<Term> getRequiredTerms() {
        return jpaQueryFactory
                .selectFrom(term)
                .where(term.used.eq(true), term.required.eq(true))
                .fetch();
    }
}
