package com.delgo.reward.comm.async;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.mongoDomain.Classification;
import com.delgo.reward.mongoService.ClassificationService;
import com.delgo.reward.service.cert.CertQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassificationAsyncService {
    private final CertQueryService certQueryService;
    private final ClassificationService classificationService;

    @Async
    public void doClassification(int certificationId){
        Certification certification = certQueryService.getOneById(certificationId);
        Classification classification = classificationService.create(certification);
    }
}
