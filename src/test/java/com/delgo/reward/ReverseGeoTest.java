package com.delgo.reward;


import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.domain.common.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReverseGeoTest {

    @Autowired
    private ReverseGeoService reverseGeoService;

    @Test
    public void getReverseGeoDataTest() {
        //given
        Location location = new Location();
        location.setLatitude("127.1311662");
        location.setLongitude("37.4410656");
        location.setSIGUGUN("");

        //when
        reverseGeoService.getReverseGeoData(location);

        //then
        assertFalse(location.getSIGUGUN().equals(""));
    }
}