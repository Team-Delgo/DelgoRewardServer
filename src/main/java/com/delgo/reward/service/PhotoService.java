package com.delgo.reward.service;


import com.delgo.reward.comm.CommService;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.record.common.ResponseRecord;
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

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


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
        ResponseEntity<ResponseRecord> result;

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();
            result = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, ResponseRecord.class);
            ResponseRecord<HashMap> responseRecord = result.getBody();

            log.info("statusCode : {}", result.getStatusCodeValue()); //http status code를 확인
            log.info("body : {}", responseRecord.data()); //실제 데이터 정보 확인

            return (Boolean) responseRecord.data().get("result");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> uploadCertPhotos(int certificationId, List<MultipartFile> photos) {
        int i = 1;
        List<String> urls = new ArrayList<>();

        for (MultipartFile photo : photos) {
            String fileName = certificationId + "_cert_" + i++ + "." + getExtension(photo);
            String url = switch (profiles) {
                case "real" -> "https://www.reward.delgo.pet/images/" + fileName;
                case "qa" -> "https://www.qa.delgo.pet/images/" + fileName;
                default -> "https://www.test.delgo.pet/images/" + fileName;
            };

            try {
                File file = new File(DIR + fileName);
                photo.transferTo(file); // 서버에 저장

                // 파일 권한 변경
                Path filePath = file.toPath();
                Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(filePath);
                permissions.add(PosixFilePermission.OTHERS_READ);

                Files.setPosixFilePermissions(filePath, permissions);

                urls.add(url);
            } catch (Exception e) {
                e.printStackTrace();
                throw new NullPointerException("JPG PHOTO UPLOAD ERROR");
            }
        }
        return urls;
    }

    public String uploadCertPhotoWithWebp(String fileName, File originalFile) {
        String webpfileName = fileName + ".webp";
        String ncpLink = (profiles.equals("real"))
                ? BucketName.CERTIFICATION.getUrl() + webpfileName
                : BucketName.CERTIFICATION.getTestUrl() + webpfileName;

        try {
            File webpFile = convertWebp(webpfileName, originalFile);  // filePath에서 File 불러온 뒤 webp로 변환 후 저장.
            objectStorageService.uploadObjects(BucketName.CERTIFICATION, webpfileName, DIR + webpfileName); // Upload NCP

            fileDelete(webpFile);
            return ncpLink;
        } catch (IOException e) {
            e.printStackTrace();
            throw new NullPointerException("WEBP PHOTO UPLOAD ERROR");
        }
    }

    public String uploadMungple(int mungpleId, MultipartFile photo) {
        String[] originalFilename = Objects.requireNonNull(photo.getOriginalFilename()).split("\\.");
        String fileName = originalFilename[0] + "_mungple.webp";
        String ncpLink = BucketName.MUNGPLE.getUrl() + fileName;

        try {
            File file = new File(DIR + mungpleId + "_mungple." + getExtension(photo)); // 서버에 저장
            photo.transferTo(file);

            File webpFile = convertWebp(fileName, file);  // filePath에서 File 불러온 뒤 webp로 변환 후 저장.

            objectStorageService.uploadObjects(BucketName.MUNGPLE, fileName, DIR + fileName); // Upload NCP
            fileDelete(file); // 서버에 저장된 사진 삭제
            fileDelete(webpFile);

            return setCacheInvalidation(ncpLink);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("PHOTO UPLOAD ERROR");
        }
    }


    public String uploadProfile(int userId, MultipartFile photo) {
        String fileName = userId + "_profile.webp";
        String ncpLink = (profiles.equals("real"))
                ? BucketName.PROFILE.getUrl() + fileName
                : BucketName.PROFILE.getTestUrl() + fileName;

        try {
            File file = new File(DIR + userId + "_profile." + getExtension(photo)); // 서버에 저장
            photo.transferTo(file);

            File webpFile = convertWebp(fileName, file);  // filePath에서 File 불러온 뒤 webp로 변환 후 저장.

            objectStorageService.uploadObjects(BucketName.PROFILE, fileName, DIR + fileName); // Upload NCP

            file.delete(); // 서버에 저장된 사진 삭제
            fileDelete(webpFile);

            return setCacheInvalidation(ncpLink);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("PHOTO UPLOAD ERROR");
        }
    }

    public String uploadAchievements(int achievementsId, MultipartFile photo) {
        String extension = getExtension(photo);

        String fileName = achievementsId + "_achievements.webp";
        String ncpLink = BucketName.ACHIEVEMENTS.getUrl() + fileName; // NCP Link

        try {
            File file = new File(DIR + achievementsId + "_achievements." + extension); // 서버에 저장
            photo.transferTo(file);

            File webpFile = convertWebp(fileName, file);  // filePath에서 File 불러온 뒤 webp로 변환 후 저장.

            objectStorageService.uploadObjects(BucketName.ACHIEVEMENTS, fileName, DIR + fileName); // Upload NCP

            file.delete(); // 서버에 저장된 사진 삭제
            fileDelete(webpFile);

            return setCacheInvalidation(ncpLink);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("PHOTO UPLOAD ERROR");
        }
    }

    private InputStream downloadImage(String sourceUrl) {
        InputStream in;
        try {
            URL url = new URL(sourceUrl);
            String protocol = url.getProtocol();
            String authority = url.getAuthority();
            String[] segments = url.getPath().split("/");
            for (int i = 0; i < segments.length; i++) {
                if (segments[i].matches(".*\\p{IsHangul}.*")) {
                    segments[i] = URLEncoder.encode(segments[i], StandardCharsets.UTF_8).replace("+", "%20");
                }
            }
            String encodedPath = String.join("/", segments);
            String query = url.getQuery() != null ? "?" + url.getQuery() : "";
            String fragment = url.getRef() != null ? "#" + url.getRef() : "";

            URL encodedUrl = new URL(protocol + "://" + authority + encodedPath + query + fragment);
            in = encodedUrl.openStream();
        } catch (IOException e) {
            throw new RuntimeException("Error while downloading image");
        }
        return in;
    }

    public String convertWebpFromUrl(String name, String imageUrl) throws UnsupportedEncodingException {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        String fileName = encodedName + ".webp";

        try {
            InputStream in = downloadImage(imageUrl);
            File file = new File(DIR + encodedName + ".jpg"); // 서버에 저장

            try(FileOutputStream out = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            File convertWebpFromUrl = convertWebp(fileName, file);  // filePath에서 File 불러온 뒤 webp로 변환 후 저장.
            log.info("convertWebpFromUrl convertWebpFromUrl : {}", convertWebpFromUrl);
            file.delete();

            return encodedName;
        } catch (Exception e) {
            log.error("menu_photo: {} webpFile 생성 실패",name);
            return "";
        }
    }

    public File convertWebp(String fileName, File file) throws IOException {
        return ImmutableImage.loader().fromFile(file)
                .output(WebpWriter.DEFAULT, new File(DIR + fileName));
    }

    public String setCacheInvalidation(String ncpLink){
        return ncpLink + "?" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddhhmmss")) + numberGen(4, 1); // Cache 무효화
    }

    public String getExtension(MultipartFile photo){
        String[] extension_arr = Objects.requireNonNull(photo.getContentType()).split("/"); // ex) png, jpg, jpeg

        return extension_arr[extension_arr.length - 1];
    }

    public void fileDelete(File file){
        if (!file.delete()) log.info("Failed to delete the file.");
    }
}
