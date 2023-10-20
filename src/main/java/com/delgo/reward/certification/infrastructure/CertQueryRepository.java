package com.delgo.reward.certification.infrastructure;

import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.certification.domain.Certification;
import org.springframework.data.domain.Page;

public interface CertQueryRepository {
    Page<Certification> findListByCondition(CertCondition certCondition);
}
