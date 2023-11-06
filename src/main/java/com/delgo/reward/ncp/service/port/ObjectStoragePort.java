package com.delgo.reward.ncp.service.port;

import com.delgo.reward.ncp.domain.BucketName;

public interface ObjectStoragePort {
    String uploadObjects(BucketName bucketName, String objectName, String filePath);
    void deleteObject(BucketName bucketName, String objectName);
}
