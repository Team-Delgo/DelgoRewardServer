package com.delgo.reward.mongoRepository;

import com.delgo.reward.mongoDomain.MongoMungple;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface MongoMungpleRepository extends MongoRepository<MongoMungple, String> {
}
