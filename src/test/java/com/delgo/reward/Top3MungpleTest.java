package com.delgo.reward;

import com.delgo.reward.dto.user.VisitCountDTO;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import com.delgo.reward.certification.service.port.CertRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Objects;
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
        int userId = 276;

        Pageable pageable = PageRequest.of(0, 3);

        List<VisitCountDTO> visitCountDTOList = certRepository.findVisitCountDTOList(userId, pageable);

        System.out.println("[getTop3MungpleTest] mungpleIdList =========");
        System.out.println("[getTop3MungpleTest] userVisitMungpleCountDTOList size: " + visitCountDTOList.size());
        for(VisitCountDTO visitCountDTO : visitCountDTOList){
            System.out.println("[mungpleId]: " + visitCountDTO.getMungpleId());
            System.out.println("[visitCount]: " + visitCountDTO.getVisitCount());
        }

        List<MongoMungple> mongoMungpleList = mongoMungpleRepository.findByMungpleIdIn(visitCountDTOList.stream().map(VisitCountDTO::getMungpleId).collect(Collectors.toList()));

        for(MongoMungple mongoMungple: mongoMungpleList){
            visitCountDTOList.replaceAll(e -> {
                if(Objects.equals(e.getMungpleId(), mongoMungple.getMungpleId())){
                    return e.setMungpleData(mongoMungple.getPlaceName(), mongoMungple.getPhotoUrls().get(0));
                } else {
                    return e;
                }
            });
        }

        for(VisitCountDTO visitCountDTO : visitCountDTOList){
            System.out.println("[mungpleId]: " + visitCountDTO.getMungpleId());
            System.out.println("[visitCount]: " + visitCountDTO.getVisitCount());
            System.out.println("[mungplePlaceName]: " + visitCountDTO.getPlaceName());
            System.out.println("[mungplePhotoUrl]: " + visitCountDTO.getPhotoUrl());
        }

    }
}
