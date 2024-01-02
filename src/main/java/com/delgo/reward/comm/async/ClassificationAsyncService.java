package com.delgo.reward.comm.async;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.user.CategoryCount;
import com.delgo.reward.mongoDomain.Classification;
import com.delgo.reward.mongoService.ClassificationService;
import com.delgo.reward.service.cert.CertQueryService;
import com.delgo.reward.service.user.CategoryCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassificationAsyncService {
    private final CertQueryService certQueryService;
    private final CategoryCountService categoryCountService;
    private final ClassificationService classificationService;

    @Async
    public void doClassification(int certificationId){
        Certification certification = certQueryService.getOneById(certificationId);
        Classification classification = classificationService.create(certification);
        CategoryCount categoryCount = categoryCountService.getOneByUserId(certification.getUser().getUserId());

        for(String categoryCode: classification.getCategory().keySet()){
            categoryCountService.save(categoryCount.addOne(categoryCode));
        }
    }
}
