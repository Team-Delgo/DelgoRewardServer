package com.delgo.reward.comm.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@EnableCaching
@Configuration
public class LocalCacheConfig {
    private final String MUNGPLE_CACHE_STORE = "MungpleCacheStore";
    private final String ACTIVITY_BY_CLASSIFICATION_CACHE_STORE = "ActivityByClassificationCacheStore";

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(List.of(new ConcurrentMapCache(MUNGPLE_CACHE_STORE), new ConcurrentMapCache(ACTIVITY_BY_CLASSIFICATION_CACHE_STORE)));
        return simpleCacheManager;
    }
}
