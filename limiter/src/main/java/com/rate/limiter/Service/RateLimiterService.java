package com.rate.limiter.Service;

import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
    private final TokenBucket redisTokenBucket;
    public RateLimiterService(TokenBucket redisTokenBucket) {
        this.redisTokenBucket = redisTokenBucket;
    }
    public boolean isAllowed(String clientId) {
        return redisTokenBucket.allowRequest(clientId, 10, 10, 60000);
    }
}