package com.quid.stock.repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StockRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(Long key) {
        return redisTemplate.opsForValue().setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3_000));
    }

    public void unlock(Long key) {
        redisTemplate.delete(generateKey(key));
    }

    private String generateKey(Long key) {
        return "productId::"+key.toString();
    }
}
