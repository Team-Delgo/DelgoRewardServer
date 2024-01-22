package com.delgo.reward.comm.ncp.storage;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ObjectStorageServiceTest {
    @Autowired
    ObjectStorageService objectStorageService;

    @Test
    @Order(1)
    void uploadObjects() throws IOException {
        // given
        BucketName bucketName = BucketName.DETAIL_DOG;
        String objectName = "upload_test";
        String filePath = "C:\\\\testPhoto\\\\2071_cert_2.png";

        // when
        String url = objectStorageService.uploadObjects(bucketName, objectName, filePath);
        System.out.println("url = " + url);

        // then
        URL uploadedUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) uploadedUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        connection.disconnect();

        // HTTP 상태 코드 200은 성공적인 응답을 나타냅니다.
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);
    }

    @Test
    @Order(2)
    void deleteObject() throws IOException {
        // given
        BucketName bucketName = BucketName.DETAIL_DOG;
        String objectName = "upload_test";

        // when
        objectStorageService.deleteObject(bucketName, objectName);

        // then
        String expectedUrl = "https://kr.object.ncloudstorage.com/reward-detail-resident-dog/upload_test";
        URL uploadedUrl = new URL(expectedUrl);
        HttpURLConnection connection = (HttpURLConnection) uploadedUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        connection.disconnect();

        // HTTP 상태 코드 200은 성공적인 응답을 나타냅니다.
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, responseCode);
    }
}