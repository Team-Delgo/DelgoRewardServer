package com.delgo.reward.comm.async;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.user.CategoryCount;
import com.delgo.reward.mongoDomain.Classification;
import com.delgo.reward.mongoService.ClassificationService;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassificationAsyncService {
    private final UserService userService;
    private final CertService certService;
    private final ClassificationService classificationService;

    @Async
    public void doClassification(int certificationId){
        Certification certification = certService.getCertById(certificationId);
        Classification classification = classificationService.runClassification(certification);
        CategoryCount categoryCount = userService.getCategoryCountByUserId(certification.getUser().getUserId());

        for(String categoryCode: classification.getCategory().keySet()){
            userService.categoryCountSave(categoryCount.addOne(categoryCode));
        }
    }

}
