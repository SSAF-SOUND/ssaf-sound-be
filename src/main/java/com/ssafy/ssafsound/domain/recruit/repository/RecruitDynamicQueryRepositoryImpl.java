package com.ssafy.ssafsound.domain.recruit.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.recruit.domain.Category;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.dto.GetRecruitsReqDto;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ssafy.ssafsound.domain.recruit.domain.QRecruit.recruit;
import static com.ssafy.ssafsound.domain.recruitapplication.domain.QRecruitApplication.recruitApplication;
import static com.ssafy.ssafsound.domain.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class RecruitDynamicQueryRepositoryImpl implements RecruitDynamicQueryRepository {

    @Autowired
    MetaDataConsumer metaDataConsumer;

    @PersistenceContext
    private EntityManager entityManager;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<Recruit> findRecruitByGetRecruitsReqDto(GetRecruitsReqDto dto, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Recruit> cq = cb.createQuery(Recruit.class);

        Root<Recruit> root = cq.from(Recruit.class);
        List<Predicate> predicates = new ArrayList<>();

        /* 정적 검색 조건 처리 -> 카테고리 */
        Predicate category = cb.equal(root.get("category"),  Category.valueOf(dto.getCategory().toUpperCase()));
        predicates.add(category);
        /* 동적 검색 조건 처리 -> 커서, 검색 키워드, 모집파트, 모집중 여부 */
        if(dto.getCursor() != null) {
            Predicate cursorInfo = cb.lessThan(root.get("id"), dto.getCursor());
            predicates.add(cursorInfo);
        }

        if(StringUtils.hasText(dto.getKeyword())) {
            Predicate titleContainKeyword = cb.like(root.get("title"), "%"+dto.getKeyword()+"%");
            predicates.add(titleContainKeyword);
        }

        List<String> skills = dto.getSkills();
        if(skills!=null && !skills.isEmpty()) {
            predicates.add(root.get("skills").in(skills));
        }

        List<String> recruitTypes = dto.getRecruitTypes();
        if(recruitTypes!=null && !recruitTypes.isEmpty()) {
            List<MetaData> limitTypes = recruitTypes.stream().map(
                   type->metaDataConsumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), type)
            ).collect(Collectors.toList());
            predicates.add(root.get("limitations").get("type").in(limitTypes));
        }
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(root.get("id")));

        TypedQuery<Recruit> query = entityManager.createQuery(cq);
        List<Recruit> recruits = query.setMaxResults(pageable.getPageSize()+1)
                .getResultList();

        boolean hasNext = pageable.isPaged() && recruits.size() > pageable.getPageSize();
        return new SliceImpl<>(hasNext ? recruits.subList(0, pageable.getPageSize()) : recruits, pageable, hasNext);
    }

    @Override
    public Slice<Recruit> findMemberJoinRecruitWithCursorAndPageable(Long memberId, Long cursorId, Pageable pageable) {
        List<Long> memberJoinRecruitIds = jpaQueryFactory.select(recruitApplication.recruit.id)
                .from(recruitApplication)
                .innerJoin(recruitApplication.recruit, recruit)
                .innerJoin(recruitApplication.member, member)
                .where(recruitApplication.member.id.eq(memberId), recruitApplication.matchStatus.eq(MatchStatus.DONE))
                .fetch();

        List<Recruit> recruits = jpaQueryFactory.selectFrom(recruit)
                .innerJoin(recruit.member, member)
                .where(recruit.id.in(memberJoinRecruitIds), recruit.member.id.eq(memberId))
                .limit(pageable.getPageSize()+1)
                .orderBy(recruit.id.desc())
                .fetch();

        boolean hasNext = pageable.isPaged() && recruits.size() > pageable.getPageSize();
        return new SliceImpl<>(hasNext ? recruits.subList(0, pageable.getPageSize()) : recruits, pageable, hasNext);
    }
}
