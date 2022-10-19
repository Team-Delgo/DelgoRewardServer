package com.delgo.reward.dto;


import com.delgo.reward.domain.Code;
import com.delgo.reward.domain.Mungple;
import com.delgo.reward.domain.common.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MungpleDTO {
    @NotNull private String categoryCode; // 카테고리 코드 ( ex. 카페, 음식점 .. )

    @NotNull private String placeName;
    @NotNull private String address; // 입력 주소

    public Mungple makeMungple(Location location, Code code) {
        return Mungple.builder()
                .categoryCode(this.categoryCode)
                .placeName(this.placeName)
                .geoCode(code.getCode())
                .pGeoCode(code.getPCode())
                .roadAddress(location.getRoadAddress())
                .jibunAddress(location.getJibunAddress())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }
}