package com.delgo.reward;

import com.delgo.reward.cache.MungpleCache;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.dto.mungple.MungpleResponse;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import com.delgo.reward.mongoRepository.MungpleRepository;
import com.delgo.reward.mongoService.MungpleService;
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
    private MungpleService mungpleService;
    @Autowired
    private MungpleRepository mungpleRepository;

    @Test
    public void allCacheTest() {
        Mungple mungple = mungpleService.getMungpleByMungpleId(1);

        MungpleCache cacheData = mungpleCacheService.getCacheData(1);
        if (!mungpleCacheService.isValidation(cacheData)) {
            cacheData = mungpleCacheService.updateCacheData(1, mungple);
        }
        MungpleCache newCacheData = mungpleCacheService.getCacheData(1);

        System.out.println("cacheData mungpleId: " + cacheData.getMungple().getMungpleId());
        System.out.println("cacheData expirationDate: " + cacheData.getExpirationDate());
        System.out.println("newCacheData mungpleId: " + newCacheData.getMungple().getMungpleId());
        System.out.println("newCacheData expirationDate: " + newCacheData.getExpirationDate());

        mungpleCacheService.deleteCacheData(1);
        MungpleCache deletedCacheData = mungpleCacheService.getCacheData(1);
    }

    @Test
    public void getCacheTest(){
        Mungple mungple = mungpleService.getMungpleByMungpleId(1);

        MungpleCache cacheData = mungpleCacheService.getCacheData(1);
        if (!mungpleCacheService.isValidation(cacheData)) {
            cacheData = mungpleCacheService.updateCacheData(1, mungple);
        }

        MungpleCache newCacheData = mungpleCacheService.getCacheData(1);

        System.out.println("cacheData mungpleId: " + cacheData.getMungple().getMungpleId());
        System.out.println("cacheData expirationDate: " + cacheData.getExpirationDate());
        System.out.println("newCacheData mungpleId: " + newCacheData.getMungple().getMungpleId());
        System.out.println("newCacheData expirationDate: " + newCacheData.getExpirationDate());
    }
    @Test
    public void getCacheCompareTest(){
        StopWatch stopWatch1 = new StopWatch();
        String categoryCode = CategoryCode.CA0000.getCode();

        System.out.println("=======[Database]=======");
        stopWatch1.start();

        List<MungpleResponse> mungpleResDTOList1 = mungpleService.getMungpleByCategoryCode(categoryCode);
        System.out.println("Count: " + mungpleResDTOList1.size());

        stopWatch1.stop();
        System.out.println(stopWatch1.prettyPrint());
        System.out.println("코드 실행 시간 (s): " + stopWatch1.getTotalTimeSeconds());


        StopWatch stopWatch2 = new StopWatch();

        System.out.println("=======[Cache]=======");
        stopWatch2.start();

        List<MungpleResponse> mungpleResDTOList2 = mungpleService.getMungpleByCategoryCode(categoryCode);
        System.out.println("Count: " + mungpleResDTOList2.size());

        stopWatch2.stop();
        System.out.println(stopWatch2.prettyPrint());
        System.out.println("코드 실행 시간 (s): " + stopWatch2.getTotalTimeSeconds());
    }
}
