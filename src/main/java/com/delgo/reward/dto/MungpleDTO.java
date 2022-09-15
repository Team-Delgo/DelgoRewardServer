package com.delgo.reward.dto;


import com.delgo.reward.domain.Mungple;
import com.delgo.reward.domain.common.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MungpleDTO {
    private String categoryCode; // 카테고리 코드 ( ex. 카페, 음식점 .. )

    private String placeName;
    private String address; // 입력 주소

    private String geoCode; // 지역 코드
    private String p_geoCode; // 부모 지역 코드

    public Mungple makeMungple(Location location) {
        return Mungple.builder()
                .categoryCode(this.categoryCode)
                .placeName(this.placeName)
                .geoCode(this.geoCode)
                .p_geoCode(this.p_geoCode)
                .roadAddress(location.getRoadAddress())
                .jibunAddress(location.getJibunAddress())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }
}