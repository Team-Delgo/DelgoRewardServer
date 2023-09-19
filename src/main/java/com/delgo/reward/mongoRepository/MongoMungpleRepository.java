package com.delgo.reward.mongoRepository;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface MongoMungpleRepository extends MongoRepository<MongoMungple, String> {
    Optional<MongoMungple> findByMungpleId(Integer mungpleId);
    List<MongoMungple> findByMungpleIdIn(List<Integer> mungpleIdList);
    List<MongoMungple> findByIsActive(boolean isActive);
    List<MongoMungple> findByCategoryCode(String categoryCode);
    List<MongoMungple> findByCategoryCodeAndIsActive(CategoryCode categoryCode, boolean isActive);
    boolean existsByLatitudeAndLongitude(String latitude, String longitude);
    boolean existsByMungpleId(int mungpleId);
    void deleteByMungpleId(int mungpleId);
}
