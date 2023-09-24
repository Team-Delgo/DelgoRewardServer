package com.delgo.reward;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.service.CertService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Top3MungpleTest {
    @Autowired
    private CertRepository certRepository;
    @Autowired
    private MongoMungpleRepository mongoMungpleRepository;

    @Test
    public void getTop3MungpleTest() {
        int userId = 300;
        Map<MongoMungple, Integer> visitMungpleCountMap = new HashMap<>();

        List<Integer> mungpleIdList = certRepository.findVisitTop3MungpleIdByUserId(userId);

        System.out.println("[getTop3MungpleTest] mungpleIdList =========");
        for(int mungpleId: mungpleIdList){
            System.out.println("[mungpleId]: " + mungpleId);
        }

        List<MongoMungple> mungpleList = mongoMungpleRepository.findByMungpleIdIn(mungpleIdList);
        List<MungpleResDTO> mungpleResDTOList = mungpleList.stream().map(MungpleResDTO::new).collect(Collectors.toList());

        for(MungpleResDTO mungpleResDTO: mungpleResDTOList){
            System.out.println(mungpleResDTO.getPlaceName());
        }

    }
}
