package com.delgo.reward.fake;

import com.delgo.reward.ncp.domain.BucketName;
import com.delgo.reward.ncp.service.port.ObjectStoragePort;
import org.springframework.beans.factory.annotation.Value;



public class FakeObjectStorageAdapter implements ObjectStoragePort {
    @Value("${config.profiles}")
    String profiles;

    @Override
    public String uploadObjects(BucketName bucketName, String objectName, String filePath) {
        String url =  (profiles.equals("real")) ? bucketName.getUrl() : bucketName.getTestUrl();
        return url + objectName;
    }

    @Override
    public void deleteObject(BucketName bucketName, String objectName) {
    }
}
