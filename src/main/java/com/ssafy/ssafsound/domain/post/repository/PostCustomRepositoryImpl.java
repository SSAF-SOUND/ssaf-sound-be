package com.ssafy.ssafsound.domain.post.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafsound.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.ssafy.ssafsound.domain.board.domain.QBoard.board;
import static com.ssafy.ssafsound.domain.member.domain.QMember.member;
import static com.ssafy.ssafsound.domain.post.domain.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findWithDetailsByBoardId(Long boardId, Long cursor, int size) {
        List<Tuple> tuples = jpaQueryFactory.select(post, board, member)
                .from(post)
                .innerJoin(post.board, board).fetchJoin()
                .innerJoin(post.member, member).fetchJoin()
                .where(postIdLtCursor(cursor), board.id.eq(boardId))
                .limit(size + 1)
                .orderBy(post.id.desc())
                .fetch();

        return tuples.stream()
                .map(tuple -> tuple.get(post))
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findWithDetailsFetchByBoardIdAndKeyword(Long boardId, String keyword, Long cursor, int size) {
        List<Tuple> tuples = jpaQueryFactory.select(post, board, member)
                .from(post)
                .innerJoin(post.board, board).fetchJoin()
                .innerJoin(post.member, member).fetchJoin()
                .where(postIdLtCursor(cursor), board.id.eq(boardId), containTitleOrContent(keyword))
                .limit(size + 1)
                .orderBy(post.id.desc())
                .fetch();

        return tuples.stream()
                .map(tuple -> tuple.get(post))
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findWithDetailsByMemberId(Long memberId, Long cursor, int size) {
        List<Tuple> tuples = jpaQueryFactory.select(post, board, member)
                .from(post)
                .innerJoin(post.board, board).fetchJoin()
                .innerJoin(post.member, member).fetchJoin()
                .where(postIdLtCursor(cursor), member.id.eq(memberId))
                .limit(size + 1)
                .orderBy(post.id.desc())
                .fetch();

        return tuples.stream()
                .map(tuple -> tuple.get(post))
                .collect(Collectors.toList());
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
