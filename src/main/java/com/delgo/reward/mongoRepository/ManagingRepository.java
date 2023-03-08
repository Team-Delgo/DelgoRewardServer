package com.delgo.reward.mongoRepository;

import com.delgo.reward.mongoDomain.Managing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagingRepository extends MongoRepository<Managing, String> {
}
