package com.delgo.reward.mongoRepository;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.mongoDomain.Classification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ClassificationRepository extends MongoRepository<Classification, String> {
    Optional<Classification> findClassificationByCertification_CertificationId(int certificationId);
    List<Classification> findAllByUser_UserId(int userId);
}
