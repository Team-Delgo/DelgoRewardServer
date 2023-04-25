package com.delgo.reward.mongoRepository;

import com.delgo.reward.mongoDomain.MongoMungple;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface MongoMungpleRepository extends MongoRepository<MongoMungple, String> {
}
