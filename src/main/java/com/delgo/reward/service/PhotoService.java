package com.delgo.reward.service;


import com.delgo.reward.comm.CommService;
import com.delgo.reward.comm.ncp.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService extends CommService {

    private final ObjectStorageService objectStorageService;
    private final String dir = "/var/www/delgo-reward-api/"; // dev
//    private final String dir = "C:\\workspace\\delgo\\DelogServer\\testimg\\"; // local


    // NCP에 인증 사진 Upload 후 접근 URL 반환
    public String uploadCertMultipart(int certificationId, MultipartFile photo) {
        // ex) png, jpg, jpeg
        String[] type = Objects.requireNonNull(photo.getOriginalFilename()).split("\\.");
        String extension = type[type.length - 1];
        if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("jpeg"))
            throw new NullPointerException("PHOTO EXTENSION IS WRONG");

        String fileName = certificationId + "_cert." + extension;
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

    // NCP에 인증 사진 Upload 후 접근 URL 반환
    public String  uploadCertIncodingFile(int certificationId, String photoUrl) {

        String fileName = certificationId + "_cert.jpeg";
        // NCP Link
        String link = "https://kr.object.ncloudstorage.com/delgo-reward-certification/" + fileName;

        String test = photoUrl.replace("data:image/jpeg;base64,","");
        try {
            byte[] decodedByte = Base64.getMimeDecoder().decode(test.getBytes());
            File convertFile = new File(dir + fileName);
            if (convertFile.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(convertFile);
                fos.write(decodedByte);
                fos.close();
            }

            if (convertFile.exists()) {
                // Upload NCP
                objectStorageService.uploadObjects("delgo-reward-certification", fileName, dir + fileName);

                // 서버에 저장된 사진 삭제
                convertFile.delete();
            }

            // Cache 무효화
            link += "?" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddhhmmss")) + numberGen(4, 1);
            return link;

        } catch (Exception e) {
            return "error:" + e.getMessage();
        }
    }

    // NCP에 profile Upload 후 접근 URL 반환
    public String uploadProfile(int userId, MultipartFile photo) {
        // ex) png, jpg, jpeg
        String[] type = Objects.requireNonNull(photo.getOriginalFilename()).split("\\.");
        String extension = type[type.length - 1];
        if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("jpeg"))
            throw new NullPointerException("PHOTO EXTENSION IS WRONG");

        String fileName = userId + "_pet_profile." + extension;
        String dir = "/var/www/delgo-api/";
        // NCP Link
        String link = "https://kr.object.ncloudstorage.com/delgo-pet-profile/" + fileName;

        try {
            // 서버에 저장
            File f = new File(dir + fileName);
            photo.transferTo(f);

            if (f.exists()) {
                // Upload NCP
                objectStorageService.uploadObjects("delgo-pet-profile", fileName, dir + fileName);

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
