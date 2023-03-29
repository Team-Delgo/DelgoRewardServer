package com.delgo.reward.mongoRepository;

import com.delgo.reward.mongoDomain.NaverPlace;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NaverPlaceRepository extends MongoRepository<NaverPlace, String> {
    List<NaverPlace> findByPlaceNameAndAddress(String placeName, String address);
}
