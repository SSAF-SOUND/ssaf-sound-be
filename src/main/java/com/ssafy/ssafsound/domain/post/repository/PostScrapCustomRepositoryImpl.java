package com.ssafy.ssafsound.domain.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafsound.domain.post.domain.PostScrap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ssafy.ssafsound.domain.board.domain.QBoard.board;
import static com.ssafy.ssafsound.domain.member.domain.QMember.member;
import static com.ssafy.ssafsound.domain.post.domain.QPost.post;
import static com.ssafy.ssafsound.domain.post.domain.QPostScrap.postScrap;

@Repository
@RequiredArgsConstructor
public class PostScrapCustomRepositoryImpl implements PostScrapCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostScrap> findMyScrapPosts(Long memberId, Long cursor, int size) {
        return jpaQueryFactory
                .selectFrom(postScrap)
                .join(postScrap.post, post).fetchJoin()
                .join(post.board, board).fetchJoin()
                .join(post.member, member).fetchJoin()
                .where(postScrapIdLtCursor(cursor), postScrap.member.id.eq(memberId))
                .limit(size + 1)
                .orderBy(postScrap.id.desc())
                .fetch();
    }

    private BooleanExpression postScrapIdLtCursor(Long cursor) {
        return cursor != -1 ? postScrap.id.lt(cursor) : null;
    }
}
