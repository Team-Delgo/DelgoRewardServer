package com.delgo.reward.mongoRepository;

import com.delgo.reward.domain.mungple.Mungple;
import com.delgo.reward.mongoDomain.MongoMungple;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface MongoMungpleRepository extends MongoRepository<MongoMungple, String> {
    List<MongoMungple> findByIsActive(boolean isActive);
    List<MongoMungple> findByCategoryCode(String categoryCode);
    List<MongoMungple> findByCategoryCodeAndIsActive(String categoryCode, boolean isActive);
    boolean existsByLatitudeAndLongitude(String latitude, String longitude);
    boolean existsById(String id);
}
