package com.delgo.reward;


import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.domain.common.Location;
import org.junit.Test;
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
        String address = "태평동 6428";
        String x = "127.1061672"; // 위도
        String y = "37.5068523"; // 경도

        //when
        Double distance = geoService.getDistance(address, x, y);

        //then
        assertTrue(distance > 0);
    }
}