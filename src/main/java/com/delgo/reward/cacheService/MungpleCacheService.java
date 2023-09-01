package com.delgo.reward.cacheService;

import com.delgo.reward.cache.MungpleCache;
import com.delgo.reward.mongoDomain.MongoMungple;
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
    private static final MungpleCache EMPTY_DATA = new MungpleCache();

    @Cacheable(cacheNames = "mungpleStore", key = "#key")
    public MungpleCache getCacheData(final int key) {
        log.info("[CacheService] MungpleCache key: " + key + "에 대한 캐시가 없습니다.");
        return EMPTY_DATA;
    }

    @CachePut(cacheNames = "mungpleStore", key = "#key")
    public MungpleCache updateCacheData(final int key, final MongoMungple value) {
        log.info("[CacheService] MungpleCache key: " + key + "에 대한 캐시를 업데이트 합니다.");
        MungpleCache cacheData = new MungpleCache();
        cacheData.setMongoMungple(value);
        cacheData.setExpirationDate(LocalDateTime.now().plusDays(1));
        return cacheData;
    }

    @CacheEvict(cacheNames = "mungpleStore", key = "#key")
    public boolean expireCacheData(final int key) {
        log.info("[CacheService] MungpleCache key: " + key + "에 대한 캐시를 삭제합니다.");
        return true;
    }

    public boolean isValidation(final MungpleCache cacheData) {
        return ObjectUtils.isNotEmpty(cacheData)
                && ObjectUtils.isNotEmpty(cacheData.getExpirationDate())
                && StringUtils.isNotEmpty(cacheData.getMongoMungple().getId())
                && cacheData.getExpirationDate().isAfter(LocalDateTime.now());
    }
}