package com.delgo.reward.common.infrastructure.query;

import com.delgo.reward.common.domain.CodeCondition;
import com.delgo.reward.common.infrastructure.entity.CodeEntity;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static com.delgo.reward.common.infrastructure.entity.QCodeEntity.codeEntity;


public class CodeQueryRepositoryImpl implements CodeQueryRepository {
    private final JPAQueryFactory queryFactory;

    public CodeQueryRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    public CodeEntity findOneByCondition(CodeCondition condition) {
        return queryFactory
                .selectFrom(codeEntity)
                .where(
                        codeEq(condition.getCode()),
                        pCodeEq(condition.getPCode()),
                        codeNameEq(condition.getCodeName()))
                .fetchOne();
    }

    private Predicate codeEq(String code) {
        return code == null ? null : codeEntity.code.eq(code);
    }

    private Predicate pCodeEq(String pCode) {
        return pCode == null ? null : codeEntity.pCode.eq(pCode);
    }

    private Predicate codeNameEq(String codeName) {
        return codeName == null ? null : codeEntity.codeName.eq(codeName);
    }
}