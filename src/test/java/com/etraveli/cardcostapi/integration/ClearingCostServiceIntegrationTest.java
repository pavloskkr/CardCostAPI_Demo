package com.etraveli.cardcostapi.integration;

import com.etraveli.cardcostapi.model.CardClearingCost;
import com.etraveli.cardcostapi.repository.CardClearingCostRepository;
import com.etraveli.cardcostapi.service.ClearingCostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ClearingCostServiceIntegrationTest {

    @Autowired
    private ClearingCostService clearingCostService;

    @Autowired
    private CardClearingCostRepository repository;

    @Test
    void saveAndRetrieveClearingCost() {
        // Given
        CardClearingCost cost = new CardClearingCost("GR", 15);

        // When
        clearingCostService.saveClearingCost(cost);
        CardClearingCost retrieved = clearingCostService.getClearingCost("GR");

        // Then
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getCountryCode()).isEqualTo("GR");
        assertThat(retrieved.getClearingCost()).isEqualTo(15);
    }

    @Test
    void getClearingCostNotFound() {
        // When / Then
        assertThrows(RuntimeException.class, () -> clearingCostService.getClearingCost("XX"));
    }
}
