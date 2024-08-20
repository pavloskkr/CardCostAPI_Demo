package com.etraveli.cardcostapi.integration;

import com.etraveli.cardcostapi.model.CardClearingCost;
import com.etraveli.cardcostapi.repository.CardClearingCostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CardClearingCostRepositoryIntegrationTest {

    @Autowired
    private CardClearingCostRepository repository;

    @Test
    void saveAndRetrieveClearingCost() {
        // Given
        CardClearingCost cost = new CardClearingCost("US", 5);

        // When
        repository.save(cost);
        CardClearingCost retrieved = repository.findByCountryCode("US");

        // Then
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getCountryCode()).isEqualTo("US");
        assertThat(retrieved.getClearingCost()).isEqualTo(5);
    }

    @Test
    void deleteClearingCost() {
        // Given
        CardClearingCost cost = new CardClearingCost("US", 5);
        repository.save(cost);

        // When
        repository.deleteByCountryCode("US");
        CardClearingCost retrieved = repository.findByCountryCode("US");

        // Then
        assertThat(retrieved).isNull();
    }
}
