package com.delgo.reward.domain.common;

import com.delgo.reward.domain.code.Code;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private String latitude; // 위도
    private String longitude; // 경도
    private String roadAddress; // 도로명 주소
    private String jibunAddress; // 지번 주소
    private String SIGUGUN; // 시구군 ( geoCode 조회하는데 사용 )
    private String SIDO; // 특별시,도 ( geoCode 조회하는데 사용 )

    private String geoCode; // code
    private String pGeoCode; // pCode

    public Location(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location setGeoCode(Code code) {
        this.geoCode = code.getCode();
        this.pGeoCode = code.getPCode();

        return this;
    }

    public String getCoordinate() {
        return longitude + "," + latitude;
    }
}
