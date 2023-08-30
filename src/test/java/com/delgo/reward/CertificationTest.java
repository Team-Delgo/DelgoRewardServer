package com.delgo.reward;


import com.delgo.reward.domain.certification.CertPhoto;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.repository.CertPhotoRepository;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.service.CertService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CertificationTest {

    @Autowired
    private CertService certService;
    @Autowired
    private CertRepository certRepository;
    @Autowired
    private CertPhotoRepository certPhotoRepository;

    @Test
    public void DeleteTest() {
        //given
        int certificationId = 935;

        //when
        certService.deleteCert(certificationId);

        //then
    }

    @Test
    public void migrationPhotoData(){
        List<Certification> certs = certRepository.findAll();
        for(Certification cert : certs){

            String photoUrl = cert.getPhotoUrl();
            CertPhoto certPhoto = CertPhoto.builder()
                    .certificationId(cert.getCertificationId())
                    .url(photoUrl)
                    .isCorrect(cert.getIsCorrect())
                    .build();

            certPhotoRepository.save(certPhoto);
        }
    }
}