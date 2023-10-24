package com.ssafy.ssafsound.domain.recruit.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.recruit.domain.Category;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.dto.AppliedRecruit;
import com.ssafy.ssafsound.domain.recruit.dto.GetRecruitsCursorReqDto;
import com.ssafy.ssafsound.domain.recruit.dto.GetRecruitsOffsetReqDto;
import com.ssafy.ssafsound.domain.recruit.dto.RecruitPaging;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.ssafy.ssafsound.domain.recruit.domain.QRecruit.recruit;
import static com.ssafy.ssafsound.domain.recruit.domain.QRecruitSkill.recruitSkill;
import static com.ssafy.ssafsound.domain.recruit.domain.QRecruitLimitation.recruitLimitation;
import static com.ssafy.ssafsound.domain.recruit.domain.QRecruitScrap.recruitScrap;
import static com.ssafy.ssafsound.domain.recruitapplication.domain.QRecruitApplication.recruitApplication;
import static com.ssafy.ssafsound.domain.member.domain.QMember.member;


@Repository
@RequiredArgsConstructor
@Slf4j
public class RecruitDynamicQueryRepositoryImpl implements RecruitDynamicQueryRepository {

    private final MetaDataConsumer metaDataConsumer;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<Recruit> findRecruitSliceByGetRecruitsReqDto(GetRecruitsCursorReqDto dto, Pageable pageable) {
        JPAQuery<Recruit> recruitDynamicQuery = findRecruitByGetRecruitsReqDto(dto);
        Long cursor = dto.getNextPaging();
        List<Recruit> recruits = recruitDynamicQuery
                .where(recruitIdLtThanCursor(cursor))
                .limit(pageable.getPageSize()+1)
                .orderBy(recruit.id.desc())
                .fetch();
        boolean hasNext = recruits.size() > pageable.getPageSize();
        return new SliceImpl<>(hasNext ? recruits.subList(0, pageable.getPageSize()) : recruits, pageable, hasNext);
    }

    @Override
    public Page<Recruit> findRecruitPageByGetRecruitsReqDto(GetRecruitsOffsetReqDto dto, Pageable pageable) {
        JPAQuery<Recruit> recruitDynamicQuery = findRecruitByGetRecruitsReqDto(dto);
        List<Recruit> recruits = recruitDynamicQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(recruit.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(recruit.count())
                .from(recruit)
                .where(
                        recruitCategoryEq(dto.getCategory()),
                        recruitTitleEq(dto.getKeyword()),
                        recruit.finishedRecruit.eq(dto.isFinished()),
                        recruitTypeContains(dto.getCategory(), dto.getRecruitTypes()),
                        recruitSkillContains(dto.getSkills())
                );

        return PageableExecutionUtils.getPage(recruits, pageable, countQuery::fetchOne);
    }

    private JPAQuery<Recruit> findRecruitByGetRecruitsReqDto(RecruitPaging<? extends Number> dto) {
        JPAQuery<Recruit> recruitDynamicQuery = jpaQueryFactory.selectFrom(recruit);
        recruitDynamicQuery
                .where(
                        recruitCategoryEq(dto.getCategory()),
                        recruitTitleEq(dto.getKeyword()),
                        recruit.finishedRecruit.eq(dto.isFinished()),
                        recruitTypeContains(dto.getCategory(), dto.getRecruitTypes()),
                        recruitSkillContains(dto.getSkills())
                );
        return recruitDynamicQuery;
    }

    private BooleanExpression recruitCategoryEq(String category) {
        return category == null ? null : recruit.category.eq(Category.valueOf(category.toUpperCase()));
    }
    private BooleanExpression recruitTitleEq(String keyword) {
        return StringUtils.hasText(keyword) ? recruit.title.contains(keyword) : null;
    }
    private BooleanExpression recruitTypeContains(String category, List<String> recruitTypes) {
        boolean isNotProject = category == null || category.toUpperCase().equals(Category.PROJECT.name());
        boolean isEmpty = recruitTypes == null || recruitTypes.isEmpty();
        if(isNotProject || isEmpty) {
            return null;
        }

        String metaDataType = MetaDataType.RECRUIT_TYPE.name();
        List<MetaData> containRecruitTypes = recruitTypes.stream()
                .map(recruitType->metaDataConsumer.getMetaData(metaDataType, recruitType))
                .collect(Collectors.toList());

        JPQLQuery<Long> limitationContainRecruitIds = JPAExpressions
                .select(recruitLimitation.recruit.id)
                .from(recruitLimitation)
                .innerJoin(recruitLimitation.recruit, recruit)
                .where(recruitLimitation.type.in(containRecruitTypes));

        return recruit.id.in(limitationContainRecruitIds);
    }

    private BooleanExpression recruitSkillContains(List<String> skills) {
        boolean isEmpty = skills==null || skills.isEmpty();
        if(isEmpty) {
            return null;
        }

        String metaDataType = MetaDataType.SKILL.name();
        List<MetaData> containSkills = skills.stream()
                .map(skillName->metaDataConsumer.getMetaData(metaDataType, skillName))
                .collect(Collectors.toList());

        JPQLQuery<Long> recruitSkillContainRecruitIds = JPAExpressions
                .select(recruitSkill.recruit.id)
                .from(recruitSkill)
                .innerJoin(recruitSkill.recruit, recruit)
                .where(recruitSkill.skill.in(containSkills));
        return recruit.id.in(recruitSkillContainRecruitIds);
    }

    @Override
    public Slice<Recruit> findMemberJoinRecruitWithCursorAndPageable(Long memberId, String category, Long cursor, Pageable pageable) {
        BooleanExpression applicationRecruitCategoryEq =
                category == null ? null :
                recruitApplication.recruit.category.eq(Category.valueOf(category.toUpperCase()));

        List<Long> memberJoinRecruitIds = jpaQueryFactory.select(recruitApplication.recruit.id)
                .from(recruitApplication)
                .innerJoin(recruitApplication.recruit, recruit)
                .innerJoin(recruitApplication.member, member)
                .where(recruitApplication.member.id.eq(memberId),
                        recruitApplication.matchStatus.eq(MatchStatus.DONE),
                        applicationRecruitCategoryEq)
                .fetch();


        BooleanExpression categoryEq = category == null ? null : recruit.category.eq(Category.valueOf(category.toUpperCase()));

        List<Recruit> recruits = jpaQueryFactory.selectFrom(recruit)
                .innerJoin(recruit.member, member)
                .where(recruitIdLtThanCursor(cursor),
                        recruit.id.in(memberJoinRecruitIds).or(recruit.member.id.eq(memberId)),
                        categoryEq)
                .limit(pageable.getPageSize()+1)
                .orderBy(recruit.id.desc())
                .fetch();

        boolean hasNext = pageable.isPaged() && recruits.size() > pageable.getPageSize();
        return new SliceImpl<>(hasNext ? recruits.subList(0, pageable.getPageSize()) : recruits, pageable, hasNext);
    }

    @Override
    public Slice<Recruit> findMemberScrapRecruitsByCursor(Long memberId, Long cursor, Pageable pageable) {
        List<Recruit> recruits = jpaQueryFactory.select(recruitScrap.recruit)
                .from(recruitScrap)
                .innerJoin(recruitScrap.recruit, recruit)
                .innerJoin(recruitScrap.member, member)
                .where(recruitIdLtThanCursor(cursor), recruitScrap.member.id.eq(memberId))
                .limit(pageable.getPageSize()+1)
                .orderBy(recruit.id.desc())
                .fetch();

        boolean hasNext = pageable.isPaged() && recruits.size() > pageable.getPageSize();
        return new SliceImpl<>(hasNext ? recruits.subList(0, pageable.getPageSize()) : recruits, pageable, hasNext);
    }

    @Override
    public Page<Recruit> findMemberScrapRecruitsByPage(Long memberId, Pageable pageable) {
        return null;
    }

    @Override
    public Slice<AppliedRecruit> findMemberAppliedRecruits(Long memberId, Long cursor, String category, String matchStatus, Pageable pageable) {
        BooleanExpression categoryEq = (category == null) ? null : recruit.category.eq(Category.valueOf(category.toUpperCase()));
        BooleanExpression matchStatusEq = (matchStatus == null) ? null : recruitApplication.matchStatus.eq(MatchStatus.valueOf(matchStatus.toUpperCase()));

        List<AppliedRecruit> recruits = jpaQueryFactory.select(
                        Projections.constructor(
                                AppliedRecruit.class,
                                recruit,
                                recruitApplication.matchStatus,
                                recruitApplication.createdAt
                        )
                )
                .from(recruitApplication)
                .innerJoin(recruitApplication.recruit, recruit)
                .innerJoin(recruitApplication.member, member)
                .where(recruitIdLtThanCursor(cursor),
                        recruitApplication.member.id.eq(memberId),
                        categoryEq,
                        matchStatusEq,
                        recruitApplication.matchStatus.notIn(MatchStatus.CANCEL))
                .limit(pageable.getPageSize() + 1)
                .orderBy(recruitApplication.id.desc())
                .fetch();

        boolean hasNext = pageable.isPaged() && recruits.size() > pageable.getPageSize();
        return new SliceImpl<>(hasNext ? recruits.subList(0, pageable.getPageSize()) : recruits, pageable, hasNext);
    }

    private BooleanExpression recruitIdLtThanCursor(Long cursor) {
        return ((cursor != null) && (cursor != -1)) ? recruit.id.lt(cursor) : null;
    }
}
