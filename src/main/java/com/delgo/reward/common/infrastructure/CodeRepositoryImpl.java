package com.delgo.reward.common.infrastructure;

import com.delgo.reward.common.domain.Code;
import com.delgo.reward.common.domain.CodeCondition;
import com.delgo.reward.common.domain.CodeType;
import com.delgo.reward.common.infrastructure.entity.CodeEntity;
import com.delgo.reward.common.infrastructure.jpa.CodeJpaRepository;
import com.delgo.reward.common.service.port.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CodeRepositoryImpl implements CodeRepository {
    private final CodeJpaRepository codeJpaRepository;

    @Override
    public List<Code> findListByType(CodeType type) {
        return codeJpaRepository.findByType(type).stream().map(CodeEntity::toModel).toList();
    }

    @Override
    public Code findOneByCondition(CodeCondition codeCondition) {
        return codeJpaRepository.findOneByCondition(codeCondition).toModel();
    }
}
