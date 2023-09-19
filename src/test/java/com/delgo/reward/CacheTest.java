package com.delgo.reward;

import com.delgo.reward.cache.MungpleCache;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.cacheService.MungpleCacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTest {
    @Autowired
    private MungpleCacheService mungpleCacheService;
    @Autowired
    private MongoMungpleService mongoMungpleService;
    @Autowired
    private MongoMungpleRepository mongoMungpleRepository;

    @Test
    public void allCacheTest() {
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

        mungpleCacheService.deleteCacheData(1);
        MungpleCache deletedCacheData = mungpleCacheService.getCacheData(1);
    }

    @Test
    public void getCacheTest(){
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
    }
    @Test
    public void getCacheCompareTest(){
        StopWatch stopWatch1 = new StopWatch();
        String categoryCode = CategoryCode.CA0000.getCode();

        System.out.println("=======[Database]=======");
        stopWatch1.start();

        List<MungpleResDTO> mungpleResDTOList1 = mongoMungpleService.getMungpleByCategoryCode(categoryCode);
        System.out.println("Count: " + mungpleResDTOList1.size());

        stopWatch1.stop();
        System.out.println(stopWatch1.prettyPrint());
        System.out.println("코드 실행 시간 (s): " + stopWatch1.getTotalTimeSeconds());


        StopWatch stopWatch2 = new StopWatch();

        System.out.println("=======[Cache]=======");
        stopWatch2.start();

        List<MungpleResDTO> mungpleResDTOList2 = mongoMungpleService.getMungpleByCategoryCode(categoryCode);
        System.out.println("Count: " + mungpleResDTOList2.size());

        stopWatch2.stop();
        System.out.println(stopWatch2.prettyPrint());
        System.out.println("코드 실행 시간 (s): " + stopWatch2.getTotalTimeSeconds());
    }
}
