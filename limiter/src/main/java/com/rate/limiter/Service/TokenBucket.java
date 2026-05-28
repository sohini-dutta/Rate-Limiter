package com.rate.limiter.Service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class TokenBucket {
    private final StringRedisTemplate redisTemplate;
    public TokenBucket(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public boolean allowRequest(String key, long capacity, long refillTokens, long refillIntervalMillis) {
        long now = System.currentTimeMillis();
        String tokenKey = "rate_limit:tokens:" + key;
        String timestampKey = "rate_limit:ts:" + key;
        String tokenStr = redisTemplate.opsForValue().get(tokenKey);
        String timeStampStr = redisTemplate.opsForValue().get(timestampKey);
        double tokens = tokenStr != null ? Double.parseDouble(tokenStr) : capacity;
        long lastRefillTimestamp = timeStampStr != null ? Long.parseLong(timeStampStr) : now;
        long timePassed = now - lastRefillTimestamp;
        if (timePassed > 0) {
            double tokensToAdd = (timePassed * refillTokens) / (double) refillIntervalMillis;
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTimestamp = now;
        }
        if (tokens >= 1) {
            tokens -= 1;
            redisTemplate.opsForValue().set(tokenKey, String.valueOf(tokens), Duration.ofMinutes(10));
            redisTemplate.opsForValue().set(timestampKey, String.valueOf(lastRefillTimestamp), Duration.ofMinutes(10));
            return true;
        }
        redisTemplate.opsForValue().set(tokenKey, String.valueOf(tokens), Duration.ofMinutes(10));
        redisTemplate.opsForValue().set(timestampKey, String.valueOf(lastRefillTimestamp), Duration.ofMinutes(10));
        return false;
    }
}