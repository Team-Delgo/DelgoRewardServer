package com.delgo.reward;

import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.service.MungpleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MungpleTest {

    @Autowired
    private MungpleService mungpleService;

    @Autowired
    private ObjectStorageService objectStorageService;

    @Test
    public void deleteNcpPhotoTest() {
        //given
        int mungpleId = 67;
        String objectName = mungpleId + "_mungple.webp";

        //when
        objectStorageService.deleteObject(BucketName.MUNGPLE, objectName);

        //then
    }

    @Test
    public void deleteMungpleTest() {
        //given
        int mungpleId = 70;

        //when
        mungpleService.delete(mungpleId);

        //then
    }

    @Test
    public void deleteDuplicateMungpleTest() {
        //given

        //when
        mungpleService.deleteDuplicateMungple();

        //then
    }
}