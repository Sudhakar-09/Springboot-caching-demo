package com.weather.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weather.service.CacheInspectionService;

@RestController
@RequestMapping("/cache")
public class CacheController {

    private final CacheInspectionService cacheInspectionService;

    public CacheController(CacheInspectionService cacheInspectionService) {
        this.cacheInspectionService = cacheInspectionService;
    }

    // Get all cache names
    @GetMapping("/names")
    public List<String> getCacheNames() {
        return cacheInspectionService.getAllCacheNames();
    }

    // Get cache contents from Redis
    @GetMapping("/contents/{cacheName}")
    public Map<Object, Object> getCacheContents(@PathVariable String cacheName) {
        return cacheInspectionService.getCacheContents(cacheName);
    }

    // Get specific cache entry
    @GetMapping("/contents/{cacheName}/{key}")
    public Object getCacheEntry(@PathVariable String cacheName, @PathVariable Object key) {
        return cacheInspectionService.getCacheEntry(cacheName, key);
    }

    // Clear a specific cache in Redis
    @DeleteMapping("/evict/{cacheName}")
    public String evictCache(@PathVariable String cacheName) {
        cacheInspectionService.evictCache(cacheName);
        return "Cache '" + cacheName + "' evicted successfully!";
    }
}
