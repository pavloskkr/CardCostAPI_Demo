package com.etraveli.cardcostapi.integration;

import com.etraveli.cardcostapi.service.ClearingCostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CardProcessingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private ClearingCostService clearingCostService;

    @BeforeEach
    void setUp() {
        Mockito.reset(restTemplate, clearingCostService); // Reset mocks before each test
    }

    @Test
    void getCardClearingCost_ShouldReturnCorrectResponse_WhenApiSucceeds() throws Exception {
        String bin = "401790";
        String binlistUrl = "https://lookup.binlist.net/" + bin;

        Map<String, Object> mockApiResponse = Map.of(
                "country", Map.of("alpha2", "GR")
        );

        when(restTemplate.getForEntity(eq(binlistUrl), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(mockApiResponse, HttpStatus.OK));

        when(clearingCostService.getClearingCostFromCountryCode("GR")).thenReturn(15.0);

        mockMvc.perform(post("/api/v1/payment-card-cost")
                        .contentType("application/json")
                        .content("{\"card_number\": \"401790456233096\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("GR"))
                .andExpect(jsonPath("$.cost").value(15.0));
    }

    @Test
    void getCardClearingCost_ShouldReturnBadRequest_WhenCardNumberIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/payment-card-cost")
                        .contentType("application/json")
                        .content("{\"card_number\": \"123\"}")) // Invalid PAN (too short)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value("Card number's length must be between 8 and 19"));
    }

    @Test
    void getCardClearingCost_ShouldReturnServiceUnavailable_WhenApiFails() throws Exception {
        String bin = "401790";
        String binlistUrl = "https://lookup.binlist.net/" + bin;

        when(restTemplate.getForEntity(eq(binlistUrl), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE));

        mockMvc.perform(post("/api/v1/payment-card-cost")
                        .contentType("application/json")
                        .content("{\"card_number\": \"401790456233096\"}"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("Service Unavailable"))
                .andExpect(jsonPath("$.message").value("Failed to fetch card information"));
    }
}

