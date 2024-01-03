package com.delgo.reward;

import com.delgo.reward.domain.certification.CertPhoto;
import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.repository.CertPhotoRepository;
import com.delgo.reward.cert.repository.CertRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
public class CertPhotoTest {

    @Autowired
    private CertPhotoRepository certPhotoRepository;

    @Autowired
    private CertRepository certRepository;


    @Test
    public void migrationPhotoData() {
        List<Certification> certList = certRepository.findAll();
        for(Certification certification : certList){
            if(certification.getPhotos().isEmpty()) {
                System.out.println("certification.id = " + certification.getCertificationId());
                List<CertPhoto> certPhotoList = certPhotoRepository.findListByCertificationId(certification.getCertificationId());
                List<String> photoList = certPhotoList.stream().map(CertPhoto::getUrl).toList();
                System.out.println("photoList = " + photoList);
                certification.setPhotos(photoList);
                certRepository.save(certification);
            }
        }
    }
}