package com.delgo.reward.cert.repository;

import com.delgo.reward.cert.domain.Classification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ClassificationRepository extends MongoRepository<Classification, String> {
    Optional<Classification> findClassificationByCertification_CertificationId(int certificationId);
    List<Classification> findAllByUser_UserId(int userId);
}
