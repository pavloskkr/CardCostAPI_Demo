package com.etraveli.cardcostapi.service;

import com.etraveli.cardcostapi.dto.CardCostResponse;
import com.etraveli.cardcostapi.exception.ExternalApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;


class CardProcessingServiceTest {

    @Mock
    private ClearingCostService clearingCostService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CardProcessingService cardProcessingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculateClearingCost_ShouldThrowIllegalArgumentException_WhenPanIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            cardProcessingService.calculateClearingCost("1234"); // Less than 8 digits
        });

        assertThrows(IllegalArgumentException.class, () -> {
            cardProcessingService.calculateClearingCost("12345678901234567890"); // More than 19 digits
        });
    }

    @Test
    void calculateClearingCost_ShouldThrowExternalApiException_WhenApiFails() {
        String bin = "123456";
        String binlistUrl = "https://lookup.binlist.net/" + bin;

        when(restTemplate.getForEntity(binlistUrl, Map.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE));

        assertThrows(ExternalApiException.class, () -> {
            cardProcessingService.calculateClearingCost("1234567890123456");
        });
    }

    @Test
    void calculateClearingCost_ShouldReturnCorrectResponse_WhenApiSucceeds() {
        String bin = "401790";
        String binlistUrl = "https://lookup.binlist.net/" + bin;

        Map<String, Object> mockApiResponse = Map.of(
                "country", Map.of("alpha2", "GR")
        );

        // Use argument matchers for flexibility
        when(restTemplate.getForEntity(eq(binlistUrl), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(mockApiResponse, HttpStatus.OK));

        when(clearingCostService.getClearingCostFromCountryCode("GR")).thenReturn(15.0);

        CardCostResponse result = cardProcessingService.calculateClearingCost("401790456233096");

        assertEquals("GR", result.getCountry());
        assertEquals(15.0, result.getCost());
    }
}
