package com.etraveli.cardcostapi.repository;

import com.etraveli.cardcostapi.model.CardClearingCost;
import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class CardClearingCostRepository {

    private static final String HASH_NAME = "CardClearingCosts";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, CardClearingCost> hashOperations;

    public CardClearingCostRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void save(CardClearingCost ccc) {
        hashOperations.put(HASH_NAME, ccc.getCountryCode(), ccc);
    }

    public CardClearingCost findByCountryCode(String countryCode) {
        return hashOperations.get(HASH_NAME, countryCode);
    }

    public void deleteByCountryCode(String countryCode) {
        hashOperations.delete(HASH_NAME, countryCode);
    }

    public Map<String, CardClearingCost> findAll() {
        return hashOperations.entries(HASH_NAME);
    }
}
