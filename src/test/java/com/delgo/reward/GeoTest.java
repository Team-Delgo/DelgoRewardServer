package com.delgo.reward;


import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.domain.common.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeoTest {

    @Autowired
    private GeoService geoService;

    @Test
    public void getGeoDataTest() {
        //given
        String address = "태평동 6428";

        //when
        Location location = geoService.getGeoData(address);

        //then
        assertNotNull(location);
    }
}