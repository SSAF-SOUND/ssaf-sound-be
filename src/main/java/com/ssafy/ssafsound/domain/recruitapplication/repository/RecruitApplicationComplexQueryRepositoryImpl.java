package com.ssafy.ssafsound.domain.recruitapplication.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafsound.domain.recruitapplication.dto.RecruitApplicationElement;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.ssafy.ssafsound.domain.member.domain.QMember.member;
import static com.ssafy.ssafsound.domain.recruit.domain.QRecruit.recruit;
import static com.ssafy.ssafsound.domain.recruitapplication.domain.QRecruitApplication.recruitApplication;
import static com.ssafy.ssafsound.domain.recruit.domain.QRecruitQuestion.recruitQuestion;
import static com.ssafy.ssafsound.domain.recruit.domain.QRecruitQuestionReply.recruitQuestionReply;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RecruitApplicationComplexQueryRepositoryImpl implements RecruitApplicationComplexQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public RecruitApplicationElement findTopByRecruitIdAndMemberId(Long recruitId, Long memberId) {
        try {
            return jpaQueryFactory.select(
                            Projections
                                    .constructor(
                                            RecruitApplicationElement.class,
                                            recruit.id,
                                            recruitApplication.id,
                                            recruitApplication.type,
                                            recruitApplication.matchStatus,
                                            member,
                                            recruitQuestionReply.content,
                                            recruitQuestion.content,
                                            recruitApplication.isLike,
                                            recruitApplication.createdAt,
                                            recruitApplication.modifiedAt
                                    ))
                    .from(recruitApplication)
                    .innerJoin(recruitApplication.member, member)
                    .innerJoin(recruitApplication.recruit, recruit)
                    .leftJoin(recruitQuestion).on(recruitQuestion.recruit.id.eq(recruit.id))
                    .leftJoin(recruitQuestionReply).on(recruitQuestionReply.question.id.eq(recruitQuestion.id))
                    .where(recruit.id.eq(recruitId), member.id.eq(memberId))
                    .orderBy(recruitApplication.id.desc())
                    .limit(1)
                    .fetchOne();
        } catch (Exception e) {
            throw new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND);
        }
    }
}