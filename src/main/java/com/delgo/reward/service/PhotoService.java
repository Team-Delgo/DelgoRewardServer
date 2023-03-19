package com.delgo.reward.service;


import com.delgo.reward.comm.CommService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.dto.common.ResponseDTO;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService extends CommService {
    @Value("${config.photoDir}")
    String DIR;

    @Value("${config.profiles}")
    String profiles;

    private final ObjectStorageService objectStorageService;

    public Boolean checkCorrectPhoto(String path){
        // Flask URL
        String url = "http://localhost:5000/check-photo?path=" + path;
        ResponseEntity<ResponseDTO> result;

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
            result = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, ResponseDTO.class);
            ResponseDTO<HashMap> responseDTO = result.getBody();

            log.info("statusCode : {}", result.getStatusCodeValue()); //http status code를 확인
            log.info("body : {}", responseDTO.getData()); //실제 데이터 정보 확인

            return (Boolean) responseDTO.getData().get("result");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Encoding File Upload [ Deprecated ]
//    public String uploadCertEncodingFile(int certificationId, String photoUrl) {
//        String fileName = certificationId + "_cert.jpeg";
//        String ncpLink = BucketName.CERTIFICATION.getUrl() + fileName;
//
//        try {
//            byte[] decodedByte = Base64.getMimeDecoder().decode(photoUrl.replace("data:image/jpeg;base64,", "").getBytes());
//            File convertFile = new File(DIR + fileName);
//            if (convertFile.createNewFile()) {
//                FileOutputStream fos = new FileOutputStream(convertFile);
//                fos.write(decodedByte);
//                fos.close();
//            }
//
//            if (convertFile.exists()) {
//                objectStorageService.uploadObjects(BucketName.CERTIFICATION, fileName, DIR + fileName);  // Upload NCP
//                convertFile.delete(); // 서버에 저장된 사진 삭제
//            }
//
//            return setCacheInvalidation(ncpLink);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new NullPointerException("PHOTO UPLOAD ERROR");
//        }
//    }

    public String uploadCertMultipart(int certificationId, MultipartFile photo) {
        String[] type = Objects.requireNonNull(photo.getOriginalFilename()).split("\\."); // ex) png, jpg, jpeg
        String extension = type[type.length - 1];

        String fileName = certificationId + "_cert.webp";
        String ncpLink = (profiles.equals("real"))
                ? BucketName.CERTIFICATION.getUrl() + fileName
                : BucketName.CERTIFICATION.getTestUrl() + fileName;

        try {
            File file = new File(DIR + certificationId + "_cert." + extension);
            photo.transferTo(file); // 서버에 저장

            File webpFile = convertWebp(fileName, file);  // filePath에서 File 불러온 뒤 webp로 변환 후 저장.
            objectStorageService.uploadObjects(BucketName.CERTIFICATION, fileName, DIR + fileName); // Upload NCP

            file.delete(); // 서버에 저장된 사진.jpg 삭제
            webpFile.delete(); // 서버에 저장된 사진.webp 삭제

            return ncpLink;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("PHOTO UPLOAD ERROR");
        }
    }

    public String uploadMungple(int mungpleId, MultipartFile photo) {
        String[] type = Objects.requireNonNull(photo.getOriginalFilename()).split("\\."); // ex) png, jpg, jpeg
        String extension = type[type.length - 1];

        String fileName = mungpleId + "_mungple.webp";
        String ncpLink = BucketName.MUNGPLE.getUrl() + fileName;

        try {
            File file = new File(DIR + mungpleId + "_mungple." + extension); // 서버에 저장
            photo.transferTo(file);

            File webpFile = convertWebp(fileName, file);  // filePath에서 File 불러온 뒤 webp로 변환 후 저장.

            objectStorageService.uploadObjects(BucketName.MUNGPLE, fileName, DIR + fileName); // Upload NCP
            file.delete(); // 서버에 저장된 사진 삭제
            webpFile.delete();

            return setCacheInvalidation(ncpLink);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("PHOTO UPLOAD ERROR");
        }
    }

    public String uploadMungpleNote(int mungpleId, MultipartFile photo) {
        String fileName = mungpleId + "_mungplenote.webp";
        String ncpLink = BucketName.MUNGPLE_NOTE.getUrl() + fileName;

        try {
            File file = new File(DIR + photo.getOriginalFilename()); // 서버에 저장
            photo.transferTo(file);

            File webpFile = convertWebp(fileName, file);  // filePath에서 File 불러온 뒤 webp로 변환 후 저장.

            objectStorageService.uploadObjects(BucketName.MUNGPLE_NOTE, fileName, DIR + fileName); // Upload NCP
            file.delete(); // 서버에 저장된 사진 삭제
            webpFile.delete();

            return setCacheInvalidation(ncpLink);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("PHOTO UPLOAD ERROR");
        }
    }

    public String uploadProfile(int userId, MultipartFile photo) {
        String[] type = Objects.requireNonNull(photo.getOriginalFilename()).split("\\."); // ex) png, jpg, jpeg
        String extension = type[type.length - 1];

        String fileName = userId + "_profile.webp";
        String ncpLink = (profiles.equals("real"))
                ? BucketName.PROFILE.getUrl() + fileName
                : BucketName.PROFILE.getTestUrl() + fileName;

        try {
            File file = new File(DIR + userId + "_profile." + extension); // 서버에 저장
            photo.transferTo(file);

            File webpFile = convertWebp(fileName, file);  // filePath에서 File 불러온 뒤 webp로 변환 후 저장.

            objectStorageService.uploadObjects(BucketName.PROFILE, fileName, DIR + fileName); // Upload NCP

            file.delete(); // 서버에 저장된 사진 삭제
            webpFile.delete();

            return setCacheInvalidation(ncpLink);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("PHOTO UPLOAD ERROR");
        }
    }

    public String uploadAchievements(int achievementsId, MultipartFile photo) {
        String[] type = Objects.requireNonNull(photo.getOriginalFilename()).split("\\."); // ex) png, jpg, jpeg
        String extension = type[type.length - 1];

        String fileName = achievementsId + "_achievements.webp";
        String ncpLink = BucketName.ACHIEVEMENTS.getUrl() + fileName; // NCP Link

        try {
            File file = new File(DIR + achievementsId + "_achievements." + extension); // 서버에 저장
            photo.transferTo(file);

            File webpFile = convertWebp(fileName, file);  // filePath에서 File 불러온 뒤 webp로 변환 후 저장.

            objectStorageService.uploadObjects(BucketName.ACHIEVEMENTS, fileName, DIR + fileName); // Upload NCP

            file.delete(); // 서버에 저장된 사진 삭제
            webpFile.delete();

            return setCacheInvalidation(ncpLink);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("PHOTO UPLOAD ERROR");
        }
    }

    // jpg -> webp 변경
    public void convertWebp(MultipartFile photo) {
        String[] photoName = photo.getOriginalFilename().split("\\.");
        String fileName = photoName[0] + ".webp";

        log.info("fileName : {}", fileName);
        try {
            File file = new File(DIR + "fileName" + ".jpg"); // 서버에 저장
            photo.transferTo(file);

            File webpFile = convertWebp(fileName, file);  // filePath에서 File 불러온 뒤 webp로 변환 후 저장.
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("Convert Webp ERROR");
        }
    }

    public File convertWebp(String fileName, File file) throws IOException {
        return ImmutableImage.loader().fromFile(file)
                .output(WebpWriter.DEFAULT, new File(DIR + fileName));
    }

    String setCacheInvalidation(String ncpLink){
        return ncpLink + "?" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddhhmmss")) + numberGen(4, 1); // Cache 무효화
    }
}
