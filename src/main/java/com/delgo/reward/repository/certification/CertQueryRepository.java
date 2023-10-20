package com.delgo.reward.repository.certification;

import com.delgo.reward.domain.certification.Certification;
import org.springframework.data.domain.Page;

public interface CertQueryRepository {
    Page<Certification> findListByCondition(CertCondition certCondition);
}
