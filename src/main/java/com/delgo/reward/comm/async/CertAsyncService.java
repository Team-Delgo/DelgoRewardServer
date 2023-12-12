package com.delgo.reward.comm.async;

import com.delgo.reward.comm.ncp.greeneye.GreenEyeService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.domain.certification.CertPhoto;
import com.delgo.reward.repository.CertPhotoRepository;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertAsyncService {
    @Value("${config.photo-dir}")
    String DIR;

    private final CertService certService;
    private final PhotoService photoService;
    private final GreenEyeService greenEyeService;
    private final CertPhotoRepository certPhotoRepository;

    @Async
    public void doSomething(Integer certificationId) {
        List<CertPhoto> certPhotos = certPhotoRepository.findListByCertificationId(certificationId);
        for (CertPhoto photo : certPhotos) {
            String fileName = photoService.getFileNameFromURL(photo.getUrl()); // ex) 1359_cert_1.png

            // [DIR 사진 파일 기준] 유해 사진 체크
            boolean isCorrect = greenEyeService.isCorrect(photo.getUrl());
            photo.setIsCorrect(isCorrect);
            if (!isCorrect) {
                certService.changeIsCorrect(certificationId, false);
            }

            String uploadedURL = photoService.upload(fileName, BucketName.CERTIFICATION);
            photo.setUrl(uploadedURL);
        }
        certPhotoRepository.saveAll(certPhotos);
    }
}