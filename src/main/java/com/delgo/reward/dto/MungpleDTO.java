package com.delgo.reward.dto;


import com.delgo.reward.domain.Mungple;
import com.delgo.reward.domain.common.Location;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MungpleDTO {
    @NotNull private String categoryCode; // 카테고리 코드 ( ex. 카페, 음식점 .. )

    @NotNull private String placeName;
    @NotNull private String address; // 입력 주소

    public Mungple toEntity(Location location) {
        return Mungple.builder()
                .categoryCode(this.categoryCode)
                .placeName(this.placeName)
                .geoCode(location.getGeoCode())
                .pGeoCode(location.getPGeoCode())
                .roadAddress(location.getRoadAddress())
                .jibunAddress(location.getJibunAddress())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }
}