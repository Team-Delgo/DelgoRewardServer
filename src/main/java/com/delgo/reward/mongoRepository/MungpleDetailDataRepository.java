package com.delgo.reward.mongoRepository;

import com.delgo.reward.mongoDomain.MungpleDetailData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MungpleDetailDataRepository extends MongoRepository<MungpleDetailData, String> {
    Optional<MungpleDetailData> findByMungpleId(int mungpleId);
}
