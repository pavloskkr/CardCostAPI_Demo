package com.etraveli.cardcostapi.controller;

import com.etraveli.cardcostapi.model.CardClearingCost;
import com.etraveli.cardcostapi.service.ClearingCostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClearingCostController.class)
public class ClearingCostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClearingCostService clearingCostService;

    private CardClearingCost sampleCost;

    @BeforeEach
    void setUp() {
        sampleCost = new CardClearingCost("US", 1);
    }

    @Test
    void saveClearingCost_ShouldReturnOkStatus() throws Exception {
        // Simulate a successful service call
        doNothing().when(clearingCostService).saveClearingCost(any(CardClearingCost.class));

        mockMvc.perform(post("/api/v1/clearing-costs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"countryCode\": \"GR\", \"clearingCost\": 3}"))
                .andExpect(status().isOk());

        verify(clearingCostService, times(1)).saveClearingCost(any(CardClearingCost.class));
    }

    @Test
    void getClearingCost_ShouldReturnCostWhenExists() throws Exception {
        when(clearingCostService.getClearingCost("US")).thenReturn(sampleCost);

        mockMvc.perform(get("/api/v1/clearing-costs/US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryCode").value("US"))
                .andExpect(jsonPath("$.clearingCost").value(1));
    }

    @Test
    void getClearingCost_ShouldReturnNotFoundWhenDoesNotExist() throws Exception {
        when(clearingCostService.getClearingCost("US")).thenReturn(null);

        mockMvc.perform(get("/api/v1/clearing-costs/US"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteClearingCost_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(delete("/api/v1/clearing-costs/US"))
                .andExpect(status().isOk());

        verify(clearingCostService, times(1)).deleteClearingCost("US");
    }

    @Test
    void updateClearingCost_ShouldReturnOkStatusWhenValid() throws Exception {
        when(clearingCostService.getClearingCost("US")).thenReturn(sampleCost);

        mockMvc.perform(put("/api/v1/clearing-costs/US")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"countryCode\": \"US\", \"clearingCost\": 0.60}"))
                .andExpect(status().isOk());

        verify(clearingCostService, times(1)).updateClearingCost(any(CardClearingCost.class));
    }

    @Test
    void updateClearingCost_ShouldReturnBadRequestWhenCountryCodeMismatch() throws Exception {
        mockMvc.perform(put("/api/v1/clearing-costs/US")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"countryCode\": \"GR\", \"clearingCost\": 0.60}"))
                .andExpect(status().isBadRequest());
    }
}
