package com.delgo.reward.mongoRepository;

import com.delgo.reward.mongoDomain.Classification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassificationRepository extends MongoRepository<Classification, String> {
}
