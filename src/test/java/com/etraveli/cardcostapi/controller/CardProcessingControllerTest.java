package com.etraveli.cardcostapi.controller;

import com.etraveli.cardcostapi.dto.CardCostResponse;
import com.etraveli.cardcostapi.exception.ExternalApiException;
import com.etraveli.cardcostapi.service.CardProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardProcessingController.class)
class CardProcessingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardProcessingService cardProcessingService;

    @Test
    void getCardClearingCost_ShouldReturnBadRequest_WhenCardNumberIsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/payment-card-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"some_other_field\": \"value\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Request"))
                .andExpect(jsonPath("$.message").value("Card number is required"));
    }

    @Test
    void getCardClearingCost_ShouldReturnOk_WhenValidCardNumberIsProvided() throws Exception {
        when(cardProcessingService.calculateClearingCost("1234567890123456"))
                .thenReturn(new CardCostResponse("GR", 15.0));

        mockMvc.perform(post("/api/v1/payment-card-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"card_number\": \"1234567890123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("GR"))
                .andExpect(jsonPath("$.cost").value(15.0));
    }

    @Test
    void getCardClearingCost_ShouldReturnBadRequest_WhenIllegalArgumentExceptionIsThrown() throws Exception {
        when(cardProcessingService.calculateClearingCost("1234567890123456"))
                .thenThrow(new IllegalArgumentException("Card number's length must be between 8 and 19"));

        mockMvc.perform(post("/api/v1/payment-card-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"card_number\": \"1234567890123456\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value("Card number's length must be between 8 and 19"));
    }

    @Test
    void getCardClearingCost_ShouldReturnServiceUnavailable_WhenExternalApiExceptionIsThrown() throws Exception {
        when(cardProcessingService.calculateClearingCost("123456"))
                .thenThrow(new ExternalApiException("Failed to fetch card information"));

        mockMvc.perform(post("/api/v1/payment-card-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"card_number\": \"123456\"}"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("Service Unavailable"))
                .andExpect(jsonPath("$.message").value("Failed to fetch card information"));
    }
}
