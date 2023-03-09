package com.delgo.reward.mongoRepository;

import com.delgo.reward.mongoDomain.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends MongoRepository<Log, String> {
}
