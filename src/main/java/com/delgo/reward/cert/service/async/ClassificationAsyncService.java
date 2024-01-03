package com.delgo.reward.cert.service.async;

import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.cert.domain.Classification;
import com.delgo.reward.cert.service.ClassificationService;
import com.delgo.reward.cert.service.CertQueryService;
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
