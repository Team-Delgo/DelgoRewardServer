package com.delgo.reward.record.mungple;

import com.delgo.reward.domain.common.Location;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;

import javax.validation.constraints.NotNull;


public record MungpleRecord(
        @NotNull String address,
        @NotNull String phoneNo,
        @NotNull String categoryCode,
        @NotNull String placeName,
        String placeNameEn) {

    public MongoMungple toMongoEntity(Location location){
        return MongoMungple.builder()
                .categoryCode(this.categoryCode)
                .phoneNo(this.phoneNo)
                .placeName(this.placeName)
                .placeNameEn(this.placeNameEn)
                .geoCode(location.getGeoCode())
                .pGeoCode(location.getPGeoCode())
                .roadAddress(location.getRoadAddress())
                .jibunAddress(location.getJibunAddress())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .isActive(true)
                .build();
    }
}