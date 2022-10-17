package com.delgo.reward;


import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.service.crawling.GetGeoCodeCrawlingService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeoTest {

    @Autowired
    private GeoService geoService;
    @Autowired
    private GetGeoCodeCrawlingService getGeoCodeCrawlingService;

    @Test
    public void getGeoDataTest() {
        //given
        String address = "경기 성남시 수정구 수정로 109";

        //when
        Location location = geoService.getGeoData(address);

        //then
        assertNotNull(location);
    }

    @Test
    public void getDistanceTest() {
        //given
        String address = "서울 중구 퇴계로 252-1";
        String latitude = "37.56226353156728"; // 위도
        String longitude = "126.99963972886282"; // 경도

        //when
        Double distance = geoService.getDistance(address, longitude, latitude);

        //then
        assertTrue(distance > 0);
    }



    @org.junit.jupiter.api.Test
    public void getGeoCodeTest() {
        //given
        String url = "https://oapi.saramin.co.kr/guide/code-table2"; // 켄싱턴

        //when
        getGeoCodeCrawlingService.crawlingProcess(url);

        //then
        Assertions.assertNotNull(1);
    }
}