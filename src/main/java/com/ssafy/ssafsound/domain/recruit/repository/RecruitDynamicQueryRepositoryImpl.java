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
import com.ssafy.ssafsound.domain.recruit.dto.GetRecruitsReqDto;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
    public Slice<Recruit> findRecruitByGetRecruitsReqDto(GetRecruitsReqDto dto, Pageable pageable) {
        // cursor base pagination (value -1 or null ignore search condition)
        Long cursor = dto.getCursor();

        // recruit category (STUDY | PROJECT)
        BooleanExpression categoryEq = dto.getCategory() == null ? null : recruit.category.eq(Category.valueOf(dto.getCategory().toUpperCase()));

        // recruit title contains search keyword
        String keyword = dto.getKeyword();
        BooleanExpression titleEq = StringUtils.hasText(keyword) ? recruit.title.contains(keyword) : null;

        JPAQuery<Recruit> recruitDynamicQuery = jpaQueryFactory.selectFrom(recruit)
                .where(recruitIdLtThanCursor(cursor), categoryEq, titleEq);

        // recruit skill
        List<String> skills = dto.getSkills();
        if(skills!=null && skills.size() > 0) {
            String metaDataType = MetaDataType.SKILL.name();
            List<MetaData> containSkills = skills.stream()
                    .map(skillName->metaDataConsumer.getMetaData(metaDataType, skillName))
                    .collect(Collectors.toList());

            JPQLQuery<Long> recruitSkillContainRecruitIds = JPAExpressions
                    .select(recruitSkill.recruit.id)
                    .from(recruitSkill)
                    .innerJoin(recruitSkill.recruit, recruit)
                    .where(recruitSkill.skill.in(containSkills));

            recruitDynamicQuery.where(recruit.id.in(recruitSkillContainRecruitIds));
        }

        // recruit types limitation
        List<String> recruitTypes = dto.getRecruitTypes();
        if(dto.getCategory() != null && dto.getCategory().toUpperCase().equals(Category.PROJECT.name()) && recruitTypes!=null && !recruitTypes.isEmpty()) {
            String metaDataType = MetaDataType.RECRUIT_TYPE.name();
            List<MetaData> containRecruitTypes = recruitTypes.stream()
                    .map(recruitType->metaDataConsumer.getMetaData(metaDataType, recruitType))
                    .collect(Collectors.toList());

            JPQLQuery<Long> limitationContainRecruitIds = JPAExpressions
                    .select(recruitLimitation.recruit.id)
                    .from(recruitLimitation)
                    .innerJoin(recruitLimitation.recruit, recruit)
                    .where(recruitLimitation.type.in(containRecruitTypes));

            recruitDynamicQuery.where(recruit.id.in(limitationContainRecruitIds));
        }

        List<Recruit> recruits = recruitDynamicQuery
                .where(recruit.finishedRecruit.eq(dto.isFinished()))
                .limit(pageable.getPageSize()+1)
                .orderBy(recruit.id.desc())
                .fetch();
        boolean hasNext = pageable.isPaged() && recruits.size() > pageable.getPageSize();
        return new SliceImpl<>(hasNext ? recruits.subList(0, pageable.getPageSize()) : recruits, pageable, hasNext);
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
    public Slice<Recruit> findMemberScrapRecruits(Long memberId, Long cursor, Pageable pageable) {
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
