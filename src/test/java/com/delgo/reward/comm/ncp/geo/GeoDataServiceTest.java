package com.delgo.reward.comm.ncp.geo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class GeoDataServiceTest {
    @Autowired
    GeoDataService geoDataService;

    @Test
    void getGeoData() {
        // given
        String address = "서울특별시 송파구 송파동 54-13";

        // when
        GeoData geoData = geoDataService.getGeoData(address);

        // then
        System.out.println("geoData = " +  geoData);
    }

    @Test
    void getReverseGeoData() {
        // given
        String latitude = "37.4996921";
        String longitude = "127.1323601";

        // when
        String result = geoDataService.getReverseGeoData(latitude, longitude);

        // then
        System.out.println("result = " + result);
    }
}