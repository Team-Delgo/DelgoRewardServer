package com.delgo.reward.service;


import com.delgo.reward.comm.CommService;
import com.delgo.reward.comm.exception.FigmaException;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService extends CommService {

    @Value("${config.photo-dir}")
    String PHOTO_DIR;

    @Value("${config.photo-url}")
    String PHOTO_URL;

    @Value("${config.profiles}")
    String profiles;

    private final ObjectStorageService objectStorageService;

    public String save(String fileName, MultipartFile photo) {
        File file = saveFile(fileName, photo); // 파일 저장
        setFilePermissions(file); // 권한 처리

        return PHOTO_URL + fileName;
    }

    public String upload(String fileName, BucketName bucketName) {
        File originalFile = new File(PHOTO_DIR + fileName);
        File convertedFile = convertWebp(getBaseNameFromFileName(fileName), originalFile);

        String url = objectStorageService.uploadObjects(bucketName, convertedFile.getName(), convertedFile.getPath());

        fileDelete(originalFile);
        fileDelete(convertedFile);

        return setCacheInvalidation(url);
    }

    public String saveAndUpload(String fileName, MultipartFile photo, BucketName bucketName) {
        File savedFile = saveFile(fileName, photo); // 파일 저장
        setFilePermissions(savedFile); // 권한 처리

        String baseName = getBaseNameFromFileName(fileName);
        File convertedFile = convertWebp(baseName, savedFile);

        String url = objectStorageService.uploadObjects(bucketName, convertedFile.getName(), convertedFile.getPath());

        fileDelete(savedFile);
        fileDelete(convertedFile);

        return setCacheInvalidation(url);
    }

    public String makeCertFileName(int certificationId, MultipartFile photo, int order) {
        return certificationId + "_cert_" + order + "." + getExtension(photo);
    }

    public String makeMungpleFileName(MultipartFile photo) {
        String[] originalFilename = Objects.requireNonNull(photo.getOriginalFilename()).split("\\.");
        return originalFilename[0] + "_mungple." + getExtension(photo);
    }

    public String makeProfileFileName(int userId, MultipartFile photo) {
        return userId + "_profile." + getExtension(photo);
    }

    // 확장자 포함 Ex) 1063_cert_1.jpg
    public String getFileNameFromURL(String url) {
        try {
            String path = new URI(url).getPath();
            return path.substring(path.lastIndexOf('/') + 1);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // 확장자 포함 X Ex) 1063_cert_1
    public String getBaseNameFromFileName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    private File saveFile(String fileName, MultipartFile photo) {
        try {
            File file = new File(PHOTO_DIR + fileName);
            photo.transferTo(file); // 서버에 저장

            return file;
        } catch (Exception e) {
            throw new NullPointerException("JPG PHOTO UPLOAD ERROR");
        }
    }

    private void setFilePermissions(File file) {
        if (!profiles.equals("local")) {
            try {
                Path filePath = file.toPath();
                Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(filePath);
                permissions.add(PosixFilePermission.OTHERS_READ);

                Files.setPosixFilePermissions(filePath, permissions);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
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
            throw new FigmaException(e.getMessage());
        }
        return in;
    }

    public String convertWebpFromUrl(String name, String imageUrl) {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        String jpgFileName = encodedName + ".jpg";
        String webpFileName = encodedName + ".webp";

        try {
            InputStream in = downloadImage(imageUrl);
            File jpgFile = new File(PHOTO_DIR + jpgFileName); // 서버에 저장

            try (FileOutputStream out = new FileOutputStream(jpgFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            // jpgFilePath에서 File 불러온 뒤 webp로 변환 후 저장.
            convertWebp(webpFileName, jpgFile);
            jpgFile.delete();

            // encodedName으로 파일 탐색후 decoding한 Name으로 NCP에 Upload
            return encodedName;
        } catch (Exception e) {
            log.error("webpFile 생성 실패 : {}", name);
            throw new FigmaException(e.getMessage());
        }
    }

    public File convertWebp(String baseName, File file) {
        try {
            return ImmutableImage.loader().fromFile(file)
                    .output(WebpWriter.DEFAULT, new File(PHOTO_DIR + baseName + ".webp"));
        } catch (IOException e){
          throw new RuntimeException();
        }
    }

    public String setCacheInvalidation(String ncpLink) {
        return ncpLink + "?" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddhhmmss")) + numberGen(4, 1); // Cache 무효화
    }

    public String getExtension(MultipartFile photo) {
        String[] extension_arr = Objects.requireNonNull(photo.getContentType()).split("/"); // ex) png, jpg, jpeg
        return extension_arr[extension_arr.length - 1];
    }

    public void fileDelete(File file) {
        if (!file.delete())
            log.error("Failed to delete the file.");
    }
}
