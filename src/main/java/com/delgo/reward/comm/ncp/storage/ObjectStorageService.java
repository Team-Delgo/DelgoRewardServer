package com.delgo.reward.comm.ncp.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ObjectStorageService {

    @Value("${config.profiles}")
    String profiles;

    final String endPoint = "https://kr.object.ncloudstorage.com";
    final String regionName = "kr-standard";
    final String accessKey = "CU54eUVGT4dRhR7H1ocm";
    final String secretKey = "oCzPFBWmPMFYCf6Z9FU6iMMBtXB1RR7UdGV2BZuS";

    // S3 client
    final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .build();

    public String uploadObjects(BucketName bucketName, String objectName, String filePath) {
        try {
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
            s3.deleteObject(getBucketName(bucketName), objectName);
            log.info("Object {} has been deleted.\n", objectName);

        } catch (SdkClientException e) {
            log.error(e.getMessage());
        }
    }

    private String getBucketName(BucketName bucketName) {
        return (profiles.equals("real")) ? bucketName.getName() : bucketName.getTestName();
    }

    private String getBucketURL(BucketName bucketName) {
        return  (profiles.equals("real")) ? bucketName.getUrl() : bucketName.getTestUrl();
    }
}
