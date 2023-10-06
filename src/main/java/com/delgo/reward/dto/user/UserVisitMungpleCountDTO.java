package com.delgo.reward.dto.user;

import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class UserVisitMungpleCountDTO {
    private Integer mungpleId;
    private Long visitCount;
    private String mungplePlaceName;
    private String mungplePhotoUrl;

    public UserVisitMungpleCountDTO(Integer mungpleId, Long visitCount){
        this.mungpleId = mungpleId;
        this.visitCount = visitCount;
    }

    public UserVisitMungpleCountDTO setMungpleData(String mungplePlaceName, String mungplePhotoUrl){
        this.mungplePlaceName = mungplePlaceName;
        this.mungplePhotoUrl = mungplePhotoUrl;
        return this;
    }
}
