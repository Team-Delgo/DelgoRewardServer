//package com.delgo.reward;
//
//import com.delgo.reward.domain.certification.Certification;
//import com.delgo.reward.dto.mungple.MungpleResDTO;
//import com.delgo.reward.dto.user.UserVisitMungpleCountDTO;
//import com.delgo.reward.mongoDomain.mungple.MongoMungple;
//import com.delgo.reward.mongoRepository.MongoMungpleRepository;
//import com.delgo.reward.mongoService.MongoMungpleService;
//import com.delgo.reward.repository.CertRepository;
//import com.delgo.reward.service.CertService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class Top3MungpleTest {
//    @Autowired
//    private CertRepository certRepository;
//    @Autowired
//    private MongoMungpleRepository mongoMungpleRepository;
//
//    @Test
//    public void getTop3MungpleTest() {
//        int userId = 276;
//
//        Pageable pageable = PageRequest.of(0, 3);
//
//        List<UserVisitMungpleCountDTO> userVisitMungpleCountDTOList = certRepository.findVisitTop3MungpleIdByUserId(userId, pageable);
//
//        System.out.println("[getTop3MungpleTest] mungpleIdList =========");
//        System.out.println("[getTop3MungpleTest] userVisitMungpleCountDTOList size: " + userVisitMungpleCountDTOList.size());
//        for(UserVisitMungpleCountDTO userVisitMungpleCountDTO: userVisitMungpleCountDTOList){
//            System.out.println("[mungpleId]: " + userVisitMungpleCountDTO.getMungpleId());
//            System.out.println("[visitCount]: " + userVisitMungpleCountDTO.getVisitCount());
//        }
//
//        List<MongoMungple> mongoMungpleList = mongoMungpleRepository.findByMungpleIdIn(userVisitMungpleCountDTOList.stream().map(UserVisitMungpleCountDTO::getMungpleId).collect(Collectors.toList()));
//
//        for(MongoMungple mongoMungple: mongoMungpleList){
//            userVisitMungpleCountDTOList.replaceAll(e -> {
//                if(Objects.equals(e.getMungpleId(), mongoMungple.getMungpleId())){
//                    return e.setMungpleData(mongoMungple.getPlaceName(), mongoMungple.getPhotoUrls().get(0));
//                } else {
//                    return e;
//                }
//            });
//        }
//
//        for(UserVisitMungpleCountDTO userVisitMungpleCountDTO: userVisitMungpleCountDTOList){
//            System.out.println("[mungpleId]: " + userVisitMungpleCountDTO.getMungpleId());
//            System.out.println("[visitCount]: " + userVisitMungpleCountDTO.getVisitCount());
//            System.out.println("[mungplePlaceName]: " + userVisitMungpleCountDTO.getMungplePlaceName());
//            System.out.println("[mungplePhotoUrl]: " + userVisitMungpleCountDTO.getMungplePhotoUrl());
//        }
//
//    }
//}
