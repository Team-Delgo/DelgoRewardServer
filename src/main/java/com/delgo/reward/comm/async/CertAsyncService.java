package com.delgo.reward.comm.async;

import com.delgo.reward.certification.service.CertService;
import com.delgo.reward.certification.service.port.CertPhotoRepository;
import com.delgo.reward.certification.domain.CertPhoto;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.ncp.domain.BucketName;
import com.delgo.reward.common.service.PhotoService;
import com.delgo.reward.ncp.service.port.GreenEyePort;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertAsyncService {
    private final CertService certService;
    private final PhotoService photoService;
    private final GreenEyePort greenEyePort;
    private final CertPhotoRepository certPhotoRepository;


    @Async
    @Transactional
    public void doSomething(Integer certificationId) {
        List<CertPhoto> certPhotos = certPhotoRepository.findListByCertId(certificationId);
        for(CertPhoto photo : certPhotos) {
            String fileName = photoService.getFileNameFromURL(photo.getUrl()); // ex) 1359_cert_1.png

            // [DIR 사진 파일 기준] 유해 사진 체크
            boolean isCorrect = greenEyePort.isCorrect(photo.getUrl());
            photo.setIsCorrect(isCorrect);
            if(!isCorrect){
                Certification cert = certService.getById(certificationId);
                cert.setIsCorrect(false);
                certService.save(cert);
            }

            String uploadedURL = photoService.upload(fileName, BucketName.CERTIFICATION);
            photo.setUrl(uploadedURL);
        }
        // 결과 저장.
        certPhotoRepository.saveAll(certPhotos);
    }
}