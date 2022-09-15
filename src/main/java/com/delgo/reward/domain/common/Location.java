package com.delgo.reward.domain.common;

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

    public String getCoordinate() {
        return latitude + "," + longitude;
    }
}
