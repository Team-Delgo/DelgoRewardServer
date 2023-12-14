package com.delgo.reward.mongoRepository;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface MungpleRepository extends MongoRepository<Mungple, String> {
    Optional<Mungple> findByMungpleId(Integer mungpleId);
    Optional<Mungple> findByPlaceName(String placeName);
    List<Mungple> findListByPlaceName(String placeName);
    List<Mungple> findByMungpleIdIn(List<Integer> mungpleIdList);
    List<Mungple> findByIsActive(boolean isActive);
    List<Mungple> findByCategoryCode(String categoryCode);
    List<Mungple> findByCategoryCodeAndIsActive(CategoryCode categoryCode, boolean isActive);
    boolean existsByLatitudeAndLongitude(String latitude, String longitude);
    boolean existsByPlaceName(String placeName);
    void deleteByMungpleId(int mungpleId);
}
