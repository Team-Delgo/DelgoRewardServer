package com.delgo.reward.cacheService;

import com.delgo.reward.cache.ActivityCache;
import com.delgo.reward.comm.code.CategoryCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
public class ActivityCacheService {
    private final String ACTIVITY_BY_CLASSIFICATION_CACHE_STORE = "ActivityByClassificationCacheStore";
    private static final ActivityCache EMPTY_DATA = new ActivityCache();

    /**
     * [ACTIVITY_BY_CLASSIFICATION_CACHE_STORE] 캐시 조회
     */
    @Cacheable(cacheNames = ACTIVITY_BY_CLASSIFICATION_CACHE_STORE, key = "#key")
    public ActivityCache getCacheData(final int key) {
        log.info("[CacheService] ActivityCache key: " + key + "에 대한 캐시가 없습니다.");
        return EMPTY_DATA;
    }


    /**
     * [ACTIVITY_BY_CLASSIFICATION_CACHE_STORE] 캐시 저장 / 업데이트
     */
    @CachePut(cacheNames = ACTIVITY_BY_CLASSIFICATION_CACHE_STORE, key = "#key")
    public ActivityCache updateCacheData(final int key, final Map<CategoryCode, Integer> value) {
        log.info("[CacheService] ActivityCache key: " + key + "에 대한 캐시를 업데이트 합니다.");
        ActivityCache cacheData = new ActivityCache();
        cacheData.setActivityMapByCategoryCode(value);
        cacheData.setExpirationDate(LocalDateTime.now().plusDays(1));
        return cacheData;
    }

    /**
     * [ACTIVITY_BY_CLASSIFICATION_CACHE_STORE] 캐시 삭제
     */
    @CacheEvict(cacheNames = ACTIVITY_BY_CLASSIFICATION_CACHE_STORE, key = "#key")
    public boolean deleteCacheData(final int key) {
        log.info("[CacheService] ActivityCache key: " + key + "에 대한 캐시를 삭제합니다.");
        return true;
    }

    /**
     * [ACTIVITY_BY_CLASSIFICATION_CACHE_STORE] 캐시 데이터 유효 검증성 확인
     */
    public boolean isValidation(final ActivityCache cacheData) {
        return ObjectUtils.isNotEmpty(cacheData)
                && ObjectUtils.isNotEmpty(cacheData.getExpirationDate())
                && ObjectUtils.isNotEmpty(cacheData.getActivityMapByCategoryCode())
                && cacheData.getExpirationDate().isAfter(LocalDateTime.now());
    }
}
