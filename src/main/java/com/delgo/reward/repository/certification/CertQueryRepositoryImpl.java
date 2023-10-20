package com.delgo.reward.repository.certification;

import com.delgo.reward.domain.certification.Certification;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.delgo.reward.domain.certification.QCertification.certification;
import static com.delgo.reward.domain.user.QUser.user;
import static com.delgo.reward.domain.pet.QPet.pet;


public class CertQueryRepositoryImpl implements CertQueryRepository {

    private final JPAQueryFactory queryFactory;

    public CertQueryRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    public Page<Certification> findListByCondition(CertCondition certCondition) {
        JPAQuery<Certification> query = queryFactory
                .selectFrom(certification)
                .innerJoin(certification.user, user).fetchJoin()
                .innerJoin(user.pet, pet).fetchJoin()
                .where(
                        userIdEq(certCondition.getUserId()),
                        mungpleIdEq(certCondition.getMungpleId()),
                        isCorrectEq(certCondition.getIsCorrect()),
                        registDtBetween(certCondition.getDate())
                )
                .orderBy(certification.registDt.desc());


        if (certCondition.getPageable().isPaged()) {
            query.offset(certCondition.getPageable().getOffset())
                    .limit(certCondition.getPageable().getPageSize());
        }

        List<Certification> certList = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(certification.count())
                .from(certification)
                .where(userIdEq(certCondition.getUserId()));

        return PageableExecutionUtils.getPage(certList, certCondition.getPageable(), countQuery::fetchOne);
    }

    private Predicate userIdEq(int userId) {
        return userId == 0 ? null : certification.user.userId.eq(userId);
    }

    private Predicate mungpleIdEq(int mungpleId) {
        return mungpleId == 0 ? null : certification.mungpleId.eq(mungpleId);
    }

    private Predicate isCorrectEq(Boolean isCorrect) {
        return isCorrect == null ? null : certification.isCorrect.eq(isCorrect);
    }

    private Predicate registDtBetween(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        LocalDateTime startDateTime = localDate.atStartOfDay();
        LocalDateTime endDateTime = localDate.atTime(23, 59, 59);
        return certification.registDt.between(startDateTime, endDateTime);
    }
}
