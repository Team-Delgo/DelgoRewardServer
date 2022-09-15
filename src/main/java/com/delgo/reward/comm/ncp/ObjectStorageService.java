package com.delgo.reward.comm.ncp;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ObjectStorageService {

    final String endPoint = "https://kr.object.ncloudstorage.com";
    final String regionName = "kr-standard";
    final String accessKey = "CU54eUVGT4dRhR7H1ocm";
    final String secretKey = "oCzPFBWmPMFYCf6Z9FU6iMMBtXB1RR7UdGV2BZuS";
    // S3 client
    final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .build();

    public void createBucket() {
        String bucketName = "sample-bucket";
        try {
            // create bucket if the bucket name does not exist
            if (s3.doesBucketExistV2(bucketName)) {
                System.out.format("Bucket %s already exists.\n", bucketName);
            } else {
                s3.createBucket(bucketName);
                System.out.format("Bucket %s has been created.\n", bucketName);
            }
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    public void selectBucket() {
        try {
            List<Bucket> buckets = s3.listBuckets();
            System.out.println("Bucket List: ");
            for (Bucket bucket : buckets) {
                System.out.println("    name=" + bucket.getName() + ", creation_date=" + bucket.getCreationDate() + ", owner=" + bucket.getOwner().getId());
            }
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    public void deleteBucket() {
        String bucketName = "sample-bucket";
        try {
            // delete bucket if the bucket exists
            if (s3.doesBucketExistV2(bucketName)) {
                // delete all objects
                ObjectListing objectListing = s3.listObjects(bucketName);
                while (true) {
                    for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
                        s3.deleteObject(bucketName, summary.getKey());
                    }

                    if (objectListing.isTruncated()) {
                        objectListing = s3.listNextBatchOfObjects(objectListing);
                    } else {
                        break;
                    }
                }

                // abort incomplete multipart uploads
                MultipartUploadListing multipartUploadListing = s3.listMultipartUploads(new ListMultipartUploadsRequest(bucketName));
                while (true) {
                    for (MultipartUpload multipartUpload : multipartUploadListing.getMultipartUploads()) {
                        s3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, multipartUpload.getKey(), multipartUpload.getUploadId()));
                    }

                    if (multipartUploadListing.isTruncated()) {
                        ListMultipartUploadsRequest listMultipartUploadsRequest = new ListMultipartUploadsRequest(bucketName);
                        listMultipartUploadsRequest.withUploadIdMarker(multipartUploadListing.getNextUploadIdMarker());
                        multipartUploadListing = s3.listMultipartUploads(listMultipartUploadsRequest);
                    } else {
                        break;
                    }
                }

                s3.deleteBucket(bucketName);
                System.out.format("Bucket %s has been deleted.\n", bucketName);
            } else {
                System.out.format("Bucket %s does not exist.\n", bucketName);
            }
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    public void uploadObjects(String bucketName, String objectName, String filePath) {
        // create folder
//        String folderName = "sample-folder/";
//
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentLength(0L);
//        objectMetadata.setContentType("application/x-directory");
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, new ByteArrayInputStream(new byte[0]), objectMetadata);
//
//        try {
//            s3.putObject(putObjectRequest);
//            System.out.format("Folder %s has been created.\n", folderName);
//        } catch (SdkClientException e) {
//            e.printStackTrace();
//        }
        // upload local file
//        String objectName = "sample-object";
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName,
                    new File(filePath)).withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putObjectRequest);
            System.out.format("Object %s has been created.\n", objectName);
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    public void selectObjects(String bucketName) {
//        String bucketName = "delgo-storage";

        // list all in the bucket
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withMaxKeys(300);

            ObjectListing objectListing = s3.listObjects(listObjectsRequest);

            System.out.println("Object List:");
            while (true) {
                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    System.out.println("    name=" + objectSummary.getKey() + ", size=" + objectSummary.getSize() + ", owner=" + objectSummary.getOwner().getId());
                }

                if (objectListing.isTruncated()) {
                    objectListing = s3.listNextBatchOfObjects(objectListing);
                } else {
                    break;
                }
            }
        } catch (AmazonS3Exception e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

        // top level folders and files in the bucket
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withDelimiter("/")
                    .withMaxKeys(300);

            ObjectListing objectListing = s3.listObjects(listObjectsRequest);

            System.out.println("Folder List:");
            for (String commonPrefixes : objectListing.getCommonPrefixes()) {
                System.out.println("    name=" + commonPrefixes);
            }

            System.out.println("File List:");
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.println("    name=" + objectSummary.getKey() + ", size=" + objectSummary.getSize() + ", owner=" + objectSummary.getOwner().getId());
            }
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    public void uploadMultipart() {
        String bucketName = "delgo-storage";
        String objectName = "sample-large-object";

        File file = new File("/tmp/sample.file");
        long contentLength = file.length();
        long partSize = 10 * 1024 * 1024;

        try {
            // initialize and get upload ID
            InitiateMultipartUploadResult initiateMultipartUploadResult = s3.initiateMultipartUpload(new InitiateMultipartUploadRequest(bucketName, objectName));
            String uploadId = initiateMultipartUploadResult.getUploadId();

            // upload parts
            List<PartETag> partETagList = new ArrayList<PartETag>();

            long fileOffset = 0;
            for (int i = 1; fileOffset < contentLength; i++) {
                partSize = Math.min(partSize, (contentLength - fileOffset));

                UploadPartRequest uploadPartRequest = new UploadPartRequest()
                        .withBucketName(bucketName)
                        .withKey(objectName)
                        .withUploadId(uploadId)
                        .withPartNumber(i)
                        .withFile(file)
                        .withFileOffset(fileOffset)
                        .withPartSize(partSize);

                UploadPartResult uploadPartResult = s3.uploadPart(uploadPartRequest);
                partETagList.add(uploadPartResult.getPartETag());

                fileOffset += partSize;
            }

            // abort
            // s3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, objectName, uploadId));

            // complete
            CompleteMultipartUploadResult completeMultipartUploadResult = s3.completeMultipartUpload(new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETagList));
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    public byte[] downloadObject(String bucketName, String objectName) {
//        String bucketName = "sample-bucket";
//        String objectName = "sample-object.txt";
//        String downloadFilePath = "/tmp/sample-object.txt";

        // download object
        try {
            S3Object s3Object = s3.getObject(bucketName, objectName);
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

//            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFilePath));
//            byte[] bytesArray = new byte[4096];
//
//            int bytesRead = -1;
//            while ((bytesRead = s3ObjectInputStream.read(bytesArray)) != -1) {
//                outputStream.write(bytesArray, 0, bytesRead);
//            }
//
//            outputStream.close();
//            s3ObjectInputStream.close();
//            System.out.format("Object %s has been downloaded.\n", objectName);

            byte[] bytesArray = IOUtils.toByteArray(s3ObjectInputStream);
            return bytesArray;
        } catch (SdkClientException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteObject(String bucketName, String objectName) {
        // delete object
        try {
            s3.deleteObject(bucketName, objectName);
            System.out.format("Object %s has been deleted.\n", objectName);
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }
}
