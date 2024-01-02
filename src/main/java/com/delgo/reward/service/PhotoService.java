package com.delgo.reward.service;


import com.delgo.reward.comm.CommService;
import com.delgo.reward.comm.exception.PhotoException;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URL;
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
    private final ObjectStorageService objectStorageService;

    @Value("${config.photo-dir}")
    String PHOTO_DIR;

    @Value("${config.photo-url}")
    String PHOTO_URL;

    @Value("${config.profiles}")
    String profiles;


    public String save(String fileName, MultipartFile photo) {
        File file = saveFile(fileName, photo); // 파일 저장
        setFilePermissions(file); // 권한 처리

        return PHOTO_URL + fileName;
    }

    public String upload(String fileName, BucketName bucketName) {
        File originalFile = new File(PHOTO_DIR + fileName);
        File convertedFile = convertToWebp(getBaseNameFromFileName(fileName), originalFile);

        String url = objectStorageService.uploadObjects(bucketName, convertedFile.getName(), convertedFile.getPath());
        deleteFile(convertedFile);

        return setCacheInvalidation(url);
    }

    public String saveAndUpload(String fileName, MultipartFile photo, BucketName bucketName) {
        File savedFile = saveFile(fileName, photo); // 파일 저장
        setFilePermissions(savedFile); // 권한 처리

        return upload(savedFile.getName(), bucketName);
    }

    public String downloadAndUploadFromURL(String baseName, String url, BucketName bucketName) {
        try {
            File originalFile = downloadImageFromURL(url);
            File convertedFile = convertToWebp(getBaseNameFromFileName(originalFile.getName()), originalFile);

            String uploadedURL = objectStorageService.uploadObjects(bucketName, baseName + ".webp", convertedFile.getPath());

            deleteFile(originalFile);
            deleteFile(convertedFile);

            return uploadedURL;
        } catch (Exception e) {
            throw new PhotoException(e.getMessage());
        }
    }

    public File downloadImageFromURL(String url) {
        String fileName = getFileNameFromURL(url);
        try {
            File file = new File(PHOTO_DIR + fileName + ".jpg"); // 서버에 저장
            FileUtils.copyURLToFile(new URL(url), file);

            setFilePermissions(file); // 권한 처리
            return file;
        } catch (Exception e) {
            throw new PhotoException(e.getMessage());
        }
    }

    public String makeCertFileName(int certificationId, MultipartFile photo, int order) {
        return certificationId + "_cert_" + order + "." + getExtension(photo);
    }

    public String makeMungpleFileName(MultipartFile photo) {
        String[] originalFilename = Objects.requireNonNull(photo.getOriginalFilename()).split("\\.");
        return originalFilename[0] + "_mungple." + getExtension(photo);
    }

    public String createProfile(int userId, MultipartFile photo){
        String fileName = userId + "_profile." + getExtension(photo);
        return saveAndUpload(fileName, photo, BucketName.PROFILE);
    }

    // 확장자 포함 Ex) 1063_cert_1.jpg
    public String getFileNameFromURL(String url) {
        try {
            String path = new URI(url).getPath();
            return path.substring(path.lastIndexOf('/') + 1);
        } catch (Exception e) {
            throw new PhotoException(e.getMessage());
        }
    }

    // 확장자 포함 X Ex) 1063_cert_1
    public String getBaseNameFromFileName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    public String getExtension(MultipartFile photo) {
        String[] extensionArr = Objects.requireNonNull(photo.getContentType()).split("/"); // ex) png, jpg, jpeg
        return extensionArr[extensionArr.length - 1];
    }

    public File convertToWebp(String baseName, File file) {
        try {
            return ImmutableImage.loader().fromFile(file)
                    .output(WebpWriter.DEFAULT, new File(PHOTO_DIR + baseName + ".webp"));
        } catch (Exception e) {
            throw new PhotoException(e.getMessage());
        }
    }

    protected File saveFile(String fileName, MultipartFile photo) {
        try {
            File file = new File(PHOTO_DIR + fileName);
            photo.transferTo(file); // 서버에 저장

            return file;
        } catch (Exception e) {
            throw new PhotoException(e.getMessage());
        }
    }

    public void deleteFile(File file) {
        if (!file.delete())
            log.error("Failed to delete the file. : {}", file.getName());
    }

    public void deleteFileByName(String fileName) {
        File file = new File(PHOTO_DIR + fileName);
        if (!file.delete())
            log.error("Failed to delete the file. : {}", fileName);
    }

    protected void setFilePermissions(File file) {
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

    public String setCacheInvalidation(String ncpLink) {
        return ncpLink + "?" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddhhmmss")) + numberGen(4, 1); // Cache 무효화
    }
}
