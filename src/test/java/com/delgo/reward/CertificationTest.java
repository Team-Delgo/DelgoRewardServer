package com.delgo.reward;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.Certification;
import com.delgo.reward.domain.Code;
import com.delgo.reward.dto.CertificationDTO;
import com.delgo.reward.service.CertificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CertificationTest {

    @Autowired
    private CertificationService certificationService;

    @Test
    public void registerCertificationTest() {
        //given
        String photoUrl = "";

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
        Certification certification =
                certificationService.registerCertification(certificationDTO.makeCertification(code, photoUrl));

        //then
        assertNotNull(certification);
    }
}