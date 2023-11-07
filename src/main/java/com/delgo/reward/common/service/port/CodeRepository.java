package com.delgo.reward.common.service.port;

import com.delgo.reward.common.domain.Code;
import com.delgo.reward.common.domain.CodeCondition;
import com.delgo.reward.common.domain.CodeType;

import java.util.List;

public interface CodeRepository {
    List<Code> findListByType(CodeType type);
    Code findOneByCondition(CodeCondition codeCondition);
}