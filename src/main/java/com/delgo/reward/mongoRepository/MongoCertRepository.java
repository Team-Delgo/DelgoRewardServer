package com.delgo.reward.mongoRepository;

import com.delgo.reward.mongoDomain.MongoCert;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MongoCertRepository extends MongoRepository<MongoCert, String> {
}
