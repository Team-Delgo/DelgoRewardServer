package com.delgo.reward.comm.async;

import com.delgo.reward.comm.ncp.greeneye.GreenEyeService;
import com.delgo.reward.domain.certification.CertPhoto;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.repository.CertPhotoRepository;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.PhotoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertAsyncService {
    @Value("${config.photoDir}")
    String DIR;

    private final CertService certService;
    private final PhotoService photoService;
    private final GreenEyeService greenEyeService;
    private final CertPhotoRepository certPhotoRepository;

    @Async
    @Transactional
    public void doSomething(Integer certificationId) throws JsonProcessingException {
        List<CertPhoto> certPhotos = certPhotoRepository.findPhotosByCertificationId(certificationId);
        for(CertPhoto photo : certPhotos) {
            String jpgUrl = photo.getUrl();
            String[] jpgPath = jpgUrl.split("/");
            String jpgName = jpgPath[jpgPath.length - 1]; // ex) 1359_cert_1.png
            String fileName = jpgName.split("\\.")[0]; // ex) 1359_cert_1

            File file = new File(DIR + jpgName);

            // [DIR 사진 파일 기준] 유해 사진 체크
            boolean isCorrect = greenEyeService.checkHarmfulPhoto(jpgUrl);
            photo.setIsCorrect(isCorrect);
            if(!isCorrect){
                Certification cert = certService.getById(certificationId);
                cert.setIsCorrect(false);
            }

            String ncpLink = photoService.uploadCertPhotoWithWebp(fileName, file);
            photo.setUrl(photoService.setCacheInvalidation(ncpLink));
            certPhotoRepository.save(photo);

            // 안 쓰는 jpg 파일 삭제
            file.delete();
        }
    }
}