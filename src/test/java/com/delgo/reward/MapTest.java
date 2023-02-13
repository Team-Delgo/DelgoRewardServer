package com.delgo.reward;

import com.delgo.reward.comm.code.PGeoCode;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.service.MapService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapTest {

    @Autowired
    private MapService mapService;

    @Test
    public void getMapDataTest() {
        //given
        int userId = 0;

        //when
        Map result = mapService.getMap(userId);

        System.out.println("mungpleList : " + result.get("mungpleList")); // mungpleList :  멍플 리스트 ( 인증된 멍플은 제거된 리스트 )
        System.out.println("certNormalList : " + result.get("certNormalList"));  // certNormalList : 일반 인증 리스트 ( 하얀 테두리 )
        System.out.println("certMunpleList : " + result.get("certMunpleList")); // certMunpleList : 멍플 인증 리스트 ( 주황 테두리 )

        //then
        assertNotNull(result);
    }

    @Test
    public void test(){

        Map<String, List<Certification>> result = mapService.test();

        List<PGeoCode> pGeoCodeList = new ArrayList<>(Arrays.asList(PGeoCode.values()));

        for(PGeoCode p: pGeoCodeList){
            System.out.println("PCode: " + p.getPGeoCode() + " cert: " + result.get(p.getPGeoCode()));
        }

    }
}