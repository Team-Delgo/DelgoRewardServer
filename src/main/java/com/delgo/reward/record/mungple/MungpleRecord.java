package com.delgo.reward.record.mungple;


import com.delgo.reward.domain.Mungple;
import com.delgo.reward.domain.common.Location;

import javax.validation.constraints.NotNull;


public record MungpleRecord(
        @NotNull String address,
        @NotNull String categoryCode,
        @NotNull String placeName,
        String placeNameEn) {

    public Mungple toEntity(Location location) {
        return Mungple.builder()
                .categoryCode(this.categoryCode)
                .placeName(this.placeName)
                .placeNameEn(this.placeNameEn)
                .geoCode(location.getGeoCode())
                .pGeoCode(location.getPGeoCode())
                .roadAddress(location.getRoadAddress())
                .jibunAddress(location.getJibunAddress())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }
}