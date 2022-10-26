package com.delgo.reward;


import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.domain.common.Location;
import lombok.extern.slf4j.Slf4j;
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
        location.setLatitude("37.56225923222384");
//        location.setLatitude("37.4410656");
//        location.setLongitude("127.1311662");
        location.setLongitude("126.99961739917393");
        location.setSIGUGUN("");

        //when
        Location result = reverseGeoService.getReverseGeoData(location);

        //then
        assertFalse(location.getSIGUGUN().equals(""));
    }
}