package com.weather.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheInspectionService {

    private final CacheManager cacheManager;
    private final RedisTemplate<String, Object> redisTemplate;

    public CacheInspectionService(CacheManager cacheManager, RedisTemplate<String, Object> redisTemplate) {
        this.cacheManager = cacheManager;
        this.redisTemplate = redisTemplate;
    }

    // List all available cache namesO
    public List<String> getAllCacheNames() {
        return new ArrayList<>(cacheManager.getCacheNames());
    }

    // Retrieve all entries from a specific cache
    public Map<Object, Object> getCacheContents(String cacheName) {
        // Check Redis for actual cache contents
        Map<Object, Object> entries = new HashMap<>();
        redisTemplate.keys(cacheName + "::*").forEach(key -> {
            Object value = redisTemplate.opsForValue().get(key);
            entries.put(key, value);
        });
        return entries.isEmpty() ? Collections.singletonMap("info", "No entries found in cache.") : entries;
    }

    // Retrieve a specific entry from a cache
    public Object getCacheEntry(String cacheName, Object key) {
        return redisTemplate.opsForValue().get(cacheName + "::" + key);
    }

    // Clear a specific cache (Eviction)
    public void evictCache(String cacheName) {
        redisTemplate.delete(redisTemplate.keys(cacheName + "::*"));
    }
}