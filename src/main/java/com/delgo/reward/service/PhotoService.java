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

    private final String DIR = "/var/www/delgo-reward-api/"; // dev

    public String uploadCertMultipart(int certificationId, MultipartFile photo) {
        String[] type = Objects.requireNonNull(photo.getOriginalFilename()).split("\\."); // ex) png, jpg, jpeg
        String extension = type[type.length - 1];

        if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("jfif"))
            throw new NullPointerException("PHOTO EXTENSION IS WRONG");

        String fileName = certificationId + "_cert." + extension;
        String ncpLink = "https://kr.object.ncloudstorage.com/reward-certification/" + fileName;

        try {
            File f = new File(DIR + fileName); // 서버에 저장
            photo.transferTo(f);

            objectStorageService.uploadObjects("reward-certification", fileName, DIR + fileName); // Upload NCP
            f.delete(); // 서버에 저장된 사진 삭제

            ncpLink += "?" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddhhmmss")) + numberGen(4, 1); // Cache 무효화
            return ncpLink;
        } catch (Exception e) {
            throw new NullPointerException("PHOTO UPLOAD ERROR");
        }
    }

    // Encoding File Upload
    public String uploadCertEncodingFile(int certificationId, String photoUrl) {
        String fileName = certificationId + "_cert.jpeg";
        String ncpLink = "https://kr.object.ncloudstorage.com/reward-certification/" + fileName;

        try {
            byte[] decodedByte = Base64.getMimeDecoder().decode(photoUrl.replace("data:image/jpeg;base64,", "").getBytes());
            File convertFile = new File(DIR + fileName);
            if (convertFile.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(convertFile);
                fos.write(decodedByte);
                fos.close();
            }

            if (convertFile.exists()) {
                objectStorageService.uploadObjects("reward-certification", fileName, DIR + fileName);  // Upload NCP
                convertFile.delete(); // 서버에 저장된 사진 삭제
            }

            ncpLink += "?" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddhhmmss")) + numberGen(4, 1); // Cache 무효화
            return ncpLink;
        } catch (Exception e) {
            throw new NullPointerException("PHOTO UPLOAD ERROR");
        }
    }

    public String uploadProfile(int userId, MultipartFile photo) {
        String[] type = Objects.requireNonNull(photo.getOriginalFilename()).split("\\."); // ex) png, jpg, jpeg
        String extension = type[type.length - 1];

        if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("jfif"))
            throw new NullPointerException("PHOTO EXTENSION IS WRONG");

        String fileName = userId + "_profile." + extension;
        String ncpLink = "https://kr.object.ncloudstorage.com/reward-profile/" + fileName; // NCP Link

        try {
            File f = new File(DIR + fileName); // 서버에 저장
            photo.transferTo(f);

            objectStorageService.uploadObjects("reward-profile", fileName, DIR + fileName); // Upload NCP
            f.delete(); // 서버에 저장된 사진 삭제

            ncpLink += "?" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddhhmmss")) + numberGen(4, 1);   // Cache 무효화
            return ncpLink;
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }
    }
}
