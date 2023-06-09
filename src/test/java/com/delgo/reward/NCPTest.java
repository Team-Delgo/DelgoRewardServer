package com.delgo.reward;

import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
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

    @Test
    public void selectMungpleDetailObjectsTest() {
        //given
        String bucketName = "reward-detail-thumbnail";

        //when
        objectStorageService.selectMungpleDetailObjects(bucketName,"후키커피");

        //then
    }

}