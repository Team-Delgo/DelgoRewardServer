package com.delgo.reward.mongoRepository;

import com.delgo.reward.mongoDomain.Test;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends MongoRepository<Test, String>{
    Optional<Test> findTestById(String id);
    void deleteTestByContent(String content);
}
