package com.delgo.reward.mongoRepository;

import com.delgo.reward.mongoDomain.NaverPlace;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NaverPlaceRepository extends MongoRepository<NaverPlace, String> {
    Optional<NaverPlace> findByPlaceNameAndAddress(String placeName, String address);
}
