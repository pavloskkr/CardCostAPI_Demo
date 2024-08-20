package com.etraveli.cardcostapi.integration;

import com.etraveli.cardcostapi.model.CardClearingCost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClearingCostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void saveClearingCost_ShouldReturnOk() throws Exception {
        // Given
        String requestBody = "{\"countryCode\": \"US\", \"clearingCost\": 5}";

        // When / Then
        mockMvc.perform(post("/api/v1/clearing-costs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void getClearingCost_ShouldReturnClearingCost() throws Exception {
        // Given
        String requestBody = "{\"countryCode\": \"GR\", \"clearingCost\": 15}";

        // Save the data first
        mockMvc.perform(post("/api/v1/clearing-costs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        // When / Then
        mockMvc.perform(get("/api/v1/clearing-costs/GR")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryCode").value("GR"))
                .andExpect(jsonPath("$.clearingCost").value(15));
    }

}
