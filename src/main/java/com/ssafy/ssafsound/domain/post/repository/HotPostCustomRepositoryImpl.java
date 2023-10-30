package com.ssafy.ssafsound.domain.post.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafsound.domain.post.domain.HotPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.ssafy.ssafsound.domain.board.domain.QBoard.board;
import static com.ssafy.ssafsound.domain.member.domain.QMember.member;
import static com.ssafy.ssafsound.domain.post.domain.QHotPost.hotPost;
import static com.ssafy.ssafsound.domain.post.domain.QPost.post;
import static com.ssafy.ssafsound.domain.post.domain.QPostLike.postLike;


@Repository
@RequiredArgsConstructor
public class HotPostCustomRepositoryImpl implements HotPostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<HotPost> findHotPosts(Long cursor, int size) {
        List<Tuple> tuples = jpaQueryFactory.select(hotPost, post, board, member)
                .from(hotPost)
                .innerJoin(hotPost.post, post).fetchJoin()
                .innerJoin(post.board, board).fetchJoin()
                .innerJoin(post.member, member).fetchJoin()
                .where(postIdLtCursor(cursor))
                .limit(size + 1)
                .orderBy(hotPost.id.desc())
                .fetch();

        return tuples.stream()
                .map(tuple -> tuple.get(hotPost))
                .collect(Collectors.toList());
    }

    @Override
    public List<HotPost> findHotPostsByKeyword(String keyword, Long cursor, int size) {
        List<Tuple> tuples = jpaQueryFactory.select(hotPost, post, board, member)
                .from(hotPost)
                .innerJoin(hotPost.post, post).fetchJoin()
                .innerJoin(post.board, board).fetchJoin()
                .innerJoin(post.member, member).fetchJoin()
                .where(postIdLtCursor(cursor), containTitleOrContent(keyword))
                .limit(size + 1)
                .orderBy(hotPost.id.desc())
                .fetch();

        return tuples.stream()
                .map(tuple -> tuple.get(hotPost))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteHotPostsUnderThreshold(Long threshold) {
        jpaQueryFactory.delete(hotPost)
                .where(hotPost.in(
                        jpaQueryFactory.select(hotPost)
                                .from(hotPost)
                                .leftJoin(postLike)
                                .on(hotPost.post.eq(postLike.post))
                                .groupBy(hotPost.id)
                                .having(hotPost.id.count().lt(threshold))
                                .fetch())
                )
                .execute();
    }

    private BooleanExpression postIdLtCursor(Long cursor) {
        return cursor != -1 ? post.id.lt(cursor) : null;
    }

    private BooleanExpression containTitleOrContent(String keyword) {
        StringTemplate title = Expressions.stringTemplate("replace({0}, ' ', '')", post.title);
        StringTemplate content = Expressions.stringTemplate("replace({0}, ' ', '')", post.content);
        return title.like("%" + keyword + "%").or(content.like("%" + keyword + "%"));
    }
}
