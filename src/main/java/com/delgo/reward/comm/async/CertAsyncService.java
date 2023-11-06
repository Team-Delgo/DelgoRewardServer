package com.delgo.reward.comm.async;

import com.delgo.reward.certification.service.CertService;
import com.delgo.reward.certification.service.port.CertPhotoRepository;
import com.delgo.reward.comm.ncp.greeneye.GreenEyeService;
import com.delgo.reward.certification.domain.CertPhoto;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.common.service.PhotoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertAsyncService {
    private final CertService certService;
    private final PhotoService photoService;
    private final GreenEyeService greenEyeService;
    private final CertPhotoRepository certPhotoRepository;

    @Async
    @Transactional
    public void doSomething(Integer certificationId) throws JsonProcessingException {
        List<CertPhoto> certPhotos = certPhotoRepository.findListByCertId(certificationId);
        for(CertPhoto photo : certPhotos) {
            String fileName = photoService.getFileNameFromURL(photo.getUrl()); // ex) 1359_cert_1.png

            // [DIR 사진 파일 기준] 유해 사진 체크
            boolean isCorrect = greenEyeService.checkHarmfulPhoto(photo.getUrl());
            photo.setIsCorrect(isCorrect);
            if(!isCorrect){
                Certification cert = certService.getById(certificationId);
                cert.setIsCorrect(false);
            }

            String url = photoService.upload(fileName, BucketName.CERTIFICATION);
            photo.setUrl(url);
        }
        // 결과 저장.
        certPhotoRepository.saveAll(certPhotos);
    }
}