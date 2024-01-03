package com.delgo.reward.cert.service.async;

import com.delgo.reward.comm.ncp.greeneye.GreenEyeService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.common.service.PhotoService;
import com.delgo.reward.cert.service.CertCommandService;
import com.delgo.reward.cert.service.CertQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertAsyncService {
    @Value("${config.photo-dir}")
    String DIR;

    private final CertQueryService certQueryService;
    private final CertCommandService certCommandService;
    private final PhotoService photoService;
    private final GreenEyeService greenEyeService;

    @Async
    public void convertAndUpload(Integer certificationId) {
        Certification certification = certQueryService.getOneById(certificationId);
        List<String> photoList = certification.getPhotos();

        List<String> uploadedList = new ArrayList<>();
        for (String url : photoList) {
            String fileName = photoService.getFileNameFromURL(url); // ex) 1359_cert_1.png

            // [DIR 사진 파일 기준] 유해 사진 체크
            boolean isCorrect = greenEyeService.isCorrect(url);
            if (!isCorrect) certCommandService.changeIsCorrect(certificationId, false);

            String uploadedURL = photoService.upload(fileName, BucketName.CERTIFICATION);
            uploadedList.add(uploadedURL);
        }
        certification.setPhotos(uploadedList);
        certCommandService.save(certification);

        for (String url : photoList) {
            String fileName = photoService.getFileNameFromURL(url); // ex) 1359_cert_1.pn
            photoService.deleteFileByName(fileName);
        }
    }
}