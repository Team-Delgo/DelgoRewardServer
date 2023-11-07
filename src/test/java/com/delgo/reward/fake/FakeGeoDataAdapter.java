package com.delgo.reward.fake;

import com.delgo.reward.domain.common.GeoData;
import com.delgo.reward.ncp.service.port.GeoDataPort;


/***
 * Mungple 니드스윗을 기준으로 작성했다.
 ***/
public class FakeGeoDataAdapter implements GeoDataPort {
    @Override
    public GeoData getGeoData(String address) {
        return GeoData.builder()
                .latitude("37.5101562")
                .longitude("127.1091707")
                .roadAddress("서울특별시 송파구 백제고분로45길 22-1")
                .jibunAddress("서울특별시 송파구 송파동 54-13")
                .build();
    }

    @Override
    public String getReverseGeoData(String latitude, String longitude) {
        return "서울특별시 송파구 송파동";
    }
}
