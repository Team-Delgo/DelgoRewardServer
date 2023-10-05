package com.delgo.reward.dto.user;

import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class UserVisitMungpleCountDTO {
    private Integer mungpleId;
    private Long visitCount;
    private MongoMungple mongoMungple;

    public UserVisitMungpleCountDTO(Integer mungpleId, Long visitCount){
        this.mungpleId = mungpleId;
        this.visitCount = visitCount;
    }

    public UserVisitMungpleCountDTO setMongoMungple(MongoMungple mongoMungple){
        this.mongoMungple = mongoMungple;
        return this;
    }
}
