package com.delgo.reward.mungple.repository;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.mungple.domain.Mungple;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface MungpleRepository extends MongoRepository<Mungple, String> {
    Optional<Mungple> findOneByMungpleId(Integer mungpleId);
    Optional<Mungple> findOneByPlaceName(String placeName);
    List<Mungple> findListByMungpleIdIn(List<Integer> mungpleIdList);
    List<Mungple> findListByIsActive(boolean isActive);
    List<Mungple> findListByCategoryCodeAndIsActive(CategoryCode categoryCode, boolean isActive);
    boolean existsByLatitudeAndLongitude(String latitude, String longitude);
    boolean existsByPlaceName(String placeName);
    void deleteByMungpleId(int mungpleId);
}
