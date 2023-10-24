package com.delgo.reward.certification.infrastructure.query;

import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.certification.infrastructure.entity.CertificationEntity;
import org.springframework.data.domain.Page;

public interface CertQueryRepository {
    Page<CertificationEntity> findListByCondition(CertCondition certCondition);
}
