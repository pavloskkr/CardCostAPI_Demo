package com.etraveli.cardcostapi.service;

import com.etraveli.cardcostapi.exception.CountryCodeNotFoundException;
import com.etraveli.cardcostapi.model.CardClearingCost;
import com.etraveli.cardcostapi.repository.CardClearingCostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ClearingCostServiceTest {

    @Mock
    private CardClearingCostRepository repository;

    @InjectMocks
    private ClearingCostService service;

    private CardClearingCost sampleCost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleCost = new CardClearingCost("US", 1);
    }

    @Test
    void saveClearingCost_shouldSaveCost() {
        service.saveClearingCost(sampleCost);
        verify(repository, times(1)).save(sampleCost);
    }

    @Test
    void getClearingCost_shouldReturnCost() {
        when(repository.findByCountryCode("US")).thenReturn(sampleCost);

        CardClearingCost result = service.getClearingCost("US");

        assertEquals(sampleCost, result);
    }

    @Test
    void deleteClearingCost_shouldDeleteCost() {
        when(repository.findByCountryCode("US")).thenReturn(new CardClearingCost("US", 5.0));
        service.deleteClearingCost("US");
        verify(repository, times(1)).deleteByCountryCode("US");
    }


    @Test
    void getAllClearingCosts_shouldReturnAllCosts() {
        Map<String, CardClearingCost> sampleMap = Map.of("US", sampleCost);
        when(repository.findAll()).thenReturn(sampleMap);

        Map<String, CardClearingCost> result = service.getAllClearingCosts();

        assertEquals(sampleMap, result);
    }

    @Test
    void updateClearingCost_shouldUpdateCostWhenExists() {
        when(repository.findByCountryCode("US")).thenReturn(sampleCost);

        service.updateClearingCost(sampleCost);

        verify(repository, times(1)).save(sampleCost);
    }

    @Test
    void updateClearingCost_shouldThrowExceptionWhenCountryCodeNotFound() {
        when(repository.findByCountryCode("US")).thenReturn(null);

        CountryCodeNotFoundException exception = assertThrows(
                CountryCodeNotFoundException.class,
                () -> service.updateClearingCost(sampleCost)
        );

        assertEquals("Country code not found: US", exception.getMessage());
    }

    @Test
    void getClearingCostFromCountryCode_ShouldReturnCorrectCost_WhenCountryCodeExists() {
        when(repository.findByCountryCode("GR")).thenReturn(new CardClearingCost("GR", 15.0));

        double cost = service.getClearingCostFromCountryCode("GR");
        assertEquals(15.0, cost);
    }

    @Test
    void getClearingCostFromCountryCode_ShouldReturnDefaultCost_WhenCountryCodeDoesNotExist() {
        when(repository.findByCountryCode("XX")).thenReturn(null); // Non-existent country code
        when(repository.findByCountryCode("Others")).thenReturn(new CardClearingCost("Others", 10.0));

        double cost = service.getClearingCostFromCountryCode("XX");
        assertEquals(10.0, cost);
    }

}
