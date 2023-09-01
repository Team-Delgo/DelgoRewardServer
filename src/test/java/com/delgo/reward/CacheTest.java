package com.delgo.reward;

import com.delgo.reward.cache.MungpleCache;
import com.delgo.reward.mongoDomain.MongoMungple;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.cacheService.MungpleCacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTest {
    @Autowired
    private MungpleCacheService mungpleCacheService;
    @Autowired
    private MongoMungpleService mongoMungpleService;

    @Test
    public void getCacheTest() {
        MongoMungple mongoMungple = mongoMungpleService.getMungpleByMungpleId(1);

        MungpleCache cacheData = mungpleCacheService.getCacheData(1);
        if (!mungpleCacheService.isValidation(cacheData)) {
            cacheData = mungpleCacheService.updateCacheData(1, mongoMungple);
        }
        MungpleCache newCacheData = mungpleCacheService.getCacheData(1);

        System.out.println("cacheData mungpleId: " + cacheData.getMongoMungple().getMungpleId());
        System.out.println("cacheData expirationDate: " + cacheData.getExpirationDate());
        System.out.println("newCacheData mungpleId: " + newCacheData.getMongoMungple().getMungpleId());
        System.out.println("newCacheData expirationDate: " + newCacheData.getExpirationDate());

        mungpleCacheService.expireCacheData(1);
        MungpleCache deletedCacheData = mungpleCacheService.getCacheData(1);
    }
}
