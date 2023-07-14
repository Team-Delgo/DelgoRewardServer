package com.delgo.reward;


import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.service.CertService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CertificationTest {

    @Autowired
    private CertService certificationService;
    @Autowired
    private CertRepository certRepository;

    @Test
    public void DeleteTest() {
        //given
        int certificationId = 935;

        //when
        certificationService.deleteCert(certificationId);

        //then
    }
}