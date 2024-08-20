package com.etraveli.cardcostapi.repository;

import com.etraveli.cardcostapi.model.CardClearingCost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CardClearingCostRepositoryTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, String, CardClearingCost> hashOperations;

    @InjectMocks
    private CardClearingCostRepository repository;

    private CardClearingCost sampleCost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleCost = new CardClearingCost("US", 0.50);
        doReturn(hashOperations).when(redisTemplate).opsForHash();
        repository.init();
    }

    @Test
    void save_ShouldSaveCardClearingCost() {
        repository.save(sampleCost);
        verify(hashOperations, times(1)).put("CardClearingCosts", sampleCost.getCountryCode(), sampleCost);
    }

    @Test
    void findByCountryCode_ShouldReturnCardClearingCost() {
        when(hashOperations.get("CardClearingCosts", "US")).thenReturn(sampleCost);

        CardClearingCost result = repository.findByCountryCode("US");

        assertEquals(sampleCost, result);
    }

    @Test
    void deleteByCountryCode_shouldDeleteCardClearingCost() {
        repository.deleteByCountryCode("US");
        verify(hashOperations, times(1)).delete("CardClearingCosts", "US");
    }

    @Test
    void findAll_shouldReturnAllCardClearingCosts() {
        Map<String, CardClearingCost> sampleMap = Map.of("US", sampleCost);
        when(hashOperations.entries("CardClearingCosts")).thenReturn(sampleMap);

        Map<String, CardClearingCost> result = repository.findAll();

        assertEquals(sampleMap, result);
    }
}
