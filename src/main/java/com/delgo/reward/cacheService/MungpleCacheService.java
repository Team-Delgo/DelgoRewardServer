package com.delgo.reward.cacheService;

import com.delgo.reward.cache.MungpleCache;
import com.delgo.reward.mongoDomain.mungple.Mungple;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class MungpleCacheService {
    private final String MUNGPLE_CACHE_STORE = "MungpleCacheStore";
    private static final MungpleCache EMPTY_DATA = new MungpleCache();

    /**
     * [MUNGPLE_CACHE_STORE] 캐시 조회
     */
    @Cacheable(cacheNames = MUNGPLE_CACHE_STORE, key = "#key")
    public MungpleCache getCacheData(final int key) {
        log.info("[CacheService] MungpleCache key: " + key + "에 대한 캐시가 없습니다.");
        return EMPTY_DATA;
    }


    /**
     * [MUNGPLE_CACHE_STORE] 캐시 저장 / 업데이트
     */
    @CachePut(cacheNames = MUNGPLE_CACHE_STORE, key = "#key")
    public MungpleCache updateCacheData(final int key, final Mungple value) {
        log.info("[CacheService] MungpleCache key: " + key + "에 대한 캐시를 업데이트 합니다.");
        MungpleCache cacheData = new MungpleCache();
        cacheData.setMungple(value);
        cacheData.setExpirationDate(LocalDateTime.now().plusDays(1));
        return cacheData;
    }

    /**
     * [MUNGPLE_CACHE_STORE] 캐시 삭제
     */
    @CacheEvict(cacheNames = MUNGPLE_CACHE_STORE, key = "#key")
    public boolean deleteCacheData(final int key) {
        log.info("[CacheService] MungpleCache key: " + key + "에 대한 캐시를 삭제합니다.");
        return true;
    }

    /**
     * [MUNGPLE_CACHE_STORE] 모든 캐시 삭제
     */
    @CacheEvict(cacheNames = MUNGPLE_CACHE_STORE, allEntries = true)
    public void deleteAllCacheData() {
        log.info("[CacheService] MungpleCache의 모든 캐시를 삭제합니다.");
    }

    /**
     * [MUNGPLE_CACHE_STORE] 캐시 데이터 유효 검증성 확인
     */
    public boolean isValidation(final MungpleCache cacheData) {
        return ObjectUtils.isNotEmpty(cacheData)
                && ObjectUtils.isNotEmpty(cacheData.getExpirationDate())
                && StringUtils.isNotEmpty(cacheData.getMungple().getId())
                && cacheData.getExpirationDate().isAfter(LocalDateTime.now());
    }
}
