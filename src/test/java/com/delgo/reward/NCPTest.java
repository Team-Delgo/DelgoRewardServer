package com.delgo.reward;

import com.delgo.reward.comm.ncp.geo.GeoDataService;
import com.delgo.reward.comm.ncp.greeneye.GreenEyeService;
import com.delgo.reward.service.mungple.MungpleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
public class NCPTest {

    @Autowired
    private GreenEyeService greenEyeService;

    @Autowired
    private GeoDataService geoDataService;

    @Autowired
    private MungpleService mungpleService;


    @Test
    public void greenEyeTest() {
        String url = "https://kr.object.ncloudstorage.com/test-certification/2072_cert_2.webp?2312120931044176";
        boolean result = greenEyeService.isCorrect(url);

        System.out.println("result = " + result);
    }

    @Test
    public void reverseGeoDataTest() {
        String latitude = "37.4996921";
        String longitude = "127.1323601";
        String result = geoDataService.getReverseGeoData(latitude, longitude);

        System.out.println("result = " + result);
    }

    @Test
    public void isMungpleTest() {
        String address = "서울특별시 송파구 백제고분로45길 22-1";
        String result = String.valueOf(mungpleService.isMungpleExisting(address));

        System.out.println("result = " + result);
    }
}