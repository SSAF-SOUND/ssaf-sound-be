package com.ssafy.ssafsound.domain.post.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafsound.domain.post.domain.HotPost;
import com.ssafy.ssafsound.domain.post.domain.QHotPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.ssafy.ssafsound.domain.post.domain.QPost.post;
import static com.ssafy.ssafsound.domain.board.domain.QBoard.board;
import static com.ssafy.ssafsound.domain.member.domain.QMember.member;


@Repository
@RequiredArgsConstructor
public class HotPostCustomRepositoryImpl implements HotPostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<HotPost> findWithDetailsFetch(int cursor, int size) {
        QHotPost hotPost = QHotPost.hotPost;

        List<Tuple> tuples = jpaQueryFactory.select(hotPost, post, board, member)
                .from(hotPost)
                .innerJoin(hotPost.post, post).fetchJoin()
                .innerJoin(post.board, board).fetchJoin()
                .innerJoin(post.member, member).fetchJoin()
                .where(checkCursor(cursor))
                .limit(size + 1)
                .orderBy(post.id.desc())
                .fetch();

        List<HotPost> hotPosts = tuples.stream()
                .map(tuple -> tuple.get(hotPost))
                .collect(Collectors.toList());

        return hotPosts;
    }

    private BooleanExpression checkCursor(Integer cursor) {
        return cursor != -1 ? post.id.lt(cursor) : null;
    }
}
