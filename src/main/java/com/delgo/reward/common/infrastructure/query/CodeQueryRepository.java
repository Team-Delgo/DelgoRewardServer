package com.delgo.reward.common.infrastructure.query;

import com.delgo.reward.common.domain.CodeCondition;
import com.delgo.reward.common.infrastructure.entity.CodeEntity;

public interface CodeQueryRepository {
    CodeEntity findOneByCondition(CodeCondition condition);
}