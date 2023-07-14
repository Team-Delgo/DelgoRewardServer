package com.delgo.reward;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.domain.mungple.Mungple;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.repository.MungpleRepository;
import com.delgo.reward.service.MungpleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MungpleTest {

    @Autowired
    private MungpleService mungpleService;

    @Autowired
    private MungpleRepository mungpleRepository;

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
        List<Mungple> mungples = mungpleRepository.findAll();
        System.out.println("count :{} " + mungples.size());
        int i = 1;
        for(Mungple mungple :mungples){
            List<Mungple> result = mungpleRepository.findMungpleByJibunAddress(mungple.getJibunAddress());
            if(result.size() >= 2) {
                i++;
                System.out.println(i + " 두개 이상 존재하는 result : " + result.get(1));
                mungpleRepository.deleteById(result.get(1).getMungpleId());
            }
        }
    }

    @Test
    public void getActiveMungple() {
        List<MungpleResDTO> mungples = mungpleService.getActiveMungpleByCategoryCode(CategoryCode.TOTAL.getCode());
        System.out.println("count :{} " + mungples.size());
        for (MungpleResDTO mungple : mungples) {
            System.out.println(" mungple : " + mungple);
        }
    }

}