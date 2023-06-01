package com.delgo.reward.mongoRepository;

import com.delgo.reward.mongoDomain.MungpleDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MungpleDetailRepository extends MongoRepository<MungpleDetail, String> {
    Optional<MungpleDetail> findByMungpleId(int mungpleId);

    Boolean existsByMungpleId(int mungpleId);
}
