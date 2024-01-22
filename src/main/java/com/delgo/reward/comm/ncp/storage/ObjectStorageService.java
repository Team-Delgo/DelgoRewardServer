package com.delgo.reward.comm.ncp.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class ObjectStorageService {
    @Value("${config.profiles}")
    String PROFILES;
    @Value("${ncp.object-storage.access-key}")
    String ACCESS_KEY;
    @Value("${ncp.object-storage.secret-key}")
    String SECRET_KEY;

    public String uploadObjects(BucketName bucketName, String objectName, String filePath) {
        try {
            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://kr.object.ncloudstorage.com", "kr-standard"))
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
                    .build();

            PutObjectRequest putObjectRequest = new PutObjectRequest(getBucketName(bucketName), objectName, new File(filePath))
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            s3.putObject(putObjectRequest);
            log.info("Object {} has been created.\n", objectName);

            return getBucketURL(bucketName) + objectName;
        } catch (SdkClientException e) {
            throw new RuntimeException();
        }
    }

    public void deleteObject(BucketName bucketName, String objectName) {
        try {
            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://kr.object.ncloudstorage.com", "kr-standard"))
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
                    .build();

            s3.deleteObject(getBucketName(bucketName), objectName);
            log.info("Object {} has been deleted.\n", objectName);

        } catch (SdkClientException e) {
            log.error(e.getMessage());
        }
    }

    private String getBucketName(BucketName bucketName) {
        return (PROFILES.equals("real")) ? bucketName.getName() : bucketName.getTestName();
    }

    private String getBucketURL(BucketName bucketName) {
        return  (PROFILES.equals("real")) ? bucketName.getUrl() : bucketName.getTestUrl();
    }
}
