package com.delgo.reward;

import com.delgo.reward.comm.ncp.greeneye.GreenEyeService;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class NCPTest {

    @Autowired
    private ObjectStorageService objectStorageService;
    @Autowired
    private GreenEyeService greenEyeService;

    @Test
    public void selectMungpleDetailObjectsTest() {
        //given
        String bucketName = "reward-detail-thumbnail";

        //when
        objectStorageService.selectMungpleDetailObjects(bucketName,"후키커피");

        //then
    }

    @Test
    public void checkHarmfulPhotoUsingGreenEyeTest() throws JsonProcessingException {
        //given
        String url = "https://kr.object.ncloudstorage.com/delgo-editornote/%EA%B3%A8%EB%93%9C%ED%8E%AB%201.png";
        //when
        greenEyeService.checkHarmfulPhoto(url);

        //then
    }

}