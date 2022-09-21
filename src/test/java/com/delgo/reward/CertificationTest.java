package com.delgo.reward;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.Certification;
import com.delgo.reward.domain.Code;
import com.delgo.reward.dto.CertificationDTO;
import com.delgo.reward.repository.CertificationRepository;
import com.delgo.reward.service.CertificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CertificationTest {

    @Autowired
    private CertificationService certificationService;
    @Autowired
    private CertificationRepository certificationRepository;

    @Test
    public void registerCertificationTest() {
        //given
        Code code = new Code();
        code.setCode("101000"); // GeoCode
        code.setPCode("101000"); // p_GeoCode


        CertificationDTO certificationDTO = new CertificationDTO();
        certificationDTO.setUserId(0);
        certificationDTO.setCategoryCode(CategoryCode.CA0002.getCode());
        certificationDTO.setMungpleId(0);
        certificationDTO.setPlaceName("TEST PLACE");
        certificationDTO.setDescription("TEST 중입니다");
        certificationDTO.setLatitude("127.1061672");
        certificationDTO.setLongitude("37.5068523");

        //when
        Certification certification = certificationService.registerCertification(certificationDTO.makeCertification(code));

        //then
        assertNotNull(certification);
    }

    @Test
    public void checkMungpleCertRegisterTest() {
        //given
        int userId = 0;
        int mungpleId = 8;

        //when
        Boolean result = certificationService.checkMungpleCertRegister(userId, mungpleId);
        System.out.println("result : " + result);

        //then
        assertTrue(result);
    }

    @Test
    public void checkCertRegisterTest() {
        //given
        int userId = 0;
        String categoryCode = "CA0002";

        //when
        Boolean result = certificationService.checkCertRegister(userId, categoryCode);
        System.out.println("result : " + result);

        //then
        assertTrue(result);
    }

    @Test
    public void timeDifferenceCalculationTest() {
        //given
        LocalDate today = LocalDate.now();
        LocalDateTime testDateTime = today.atTime(4, 0, 5);
        System.out.println("testDateTime: " + testDateTime);
        System.out.println("now: " + LocalDateTime.now());

        //when
        long diffrence = ChronoUnit.SECONDS.between(testDateTime, LocalDateTime.now());
        System.out.println("diffrence: " + diffrence);

        //then
        assertTrue(diffrence < 21600);
    }

    @Test
    public void repositoryCountTest() {
        //given
        int userId = 0;
        String categoryCode = "CA0002";

        //when
        int count1 = certificationRepository.countByUserIdAndCategoryCode(userId,categoryCode);
        int count2 = certificationRepository.countByUserIdAndCategoryCodeAndMungpleId(userId, categoryCode,8);
        System.out.println("count1: " + count1);
        System.out.println("count2: " + count2);

        //then
        assertTrue(count1 < 21600);
    }
}