package com.delgo.reward.common.infrastructure.jpa;

import com.delgo.reward.common.domain.CodeType;
import com.delgo.reward.common.infrastructure.entity.CodeEntity;
import com.delgo.reward.common.infrastructure.query.CodeQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeJpaRepository extends JpaRepository<CodeEntity, Integer>, CodeQueryRepository {
    List<CodeEntity> findByType(CodeType type);
}