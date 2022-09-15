package com.delgo.reward.service;


import com.delgo.reward.comm.CommService;
import com.delgo.reward.comm.ncp.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService extends CommService {

    private final ObjectStorageService objectStorageService;

    // NCP에 인증 사진 Upload 후 접근 URL 반환
    public String uploadCertificationPhoto(int certificationId, MultipartFile photo) {
        // ex) png, jpg, jpeg
        String[] type = Objects.requireNonNull(photo.getOriginalFilename()).split("\\.");
        String extension = type[type.length - 1];
        if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("jpeg"))
            throw new NullPointerException("PHOTO EXTENSION IS WRONG");

        String fileName = certificationId + "_pet_profile." + extension;
        String dir = "/var/www/delgo-reward-api/";
        // NCP Link
        String link = "https://kr.object.ncloudstorage.com/delgo-reward-certification/" + fileName;

        try {
            // 서버에 저장
            File f = new File(dir + fileName);
            photo.transferTo(f);

            if (f.exists()) {
                // Upload NCP
                objectStorageService.uploadObjects("delgo-reward-certification", fileName, dir + fileName);

                // 서버에 저장된 사진 삭제
                f.delete();
            }

            // Cache 무효화
            link += "?" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddhhmmss")) + numberGen(4, 1);
            return link;
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }
    }
}
