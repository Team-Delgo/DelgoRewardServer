package com.delgo.reward.comm.async;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertAsyncService {
    @Value("${config.photoDir}")
    String DIR;

    private final CertService certService;
    private final PhotoService photoService;
//    private final RankingService rankingService;

    @Async
    public void doSomething(Integer certificationId) {
        Certification certification = certService.getCertById(certificationId);
        String jpgUrl = certification.getPhotoUrl();
        String[] jpgPath = jpgUrl.split("/");
        String jpgName = jpgPath[jpgPath.length -1];

        File file = new File(DIR + jpgName);

        // [DIR 사진 파일 기준] 강아지 사진 여부 체크
        certification.setIsCorrectPhoto(photoService.checkCorrectPhoto(DIR + jpgName));
        String ncpLink = photoService.uploadCertMultipartForWebp(certification.getCertificationId(), file);
        certification.setPhotoUrl(photoService.setCacheInvalidation(ncpLink));
        certService.save(certification);

        // 안 쓰는 jpg 파일 삭제
        file.delete();
        // 랭킹 실시간으로 집계
//        rankingService.rankingByPoint();
    }
}