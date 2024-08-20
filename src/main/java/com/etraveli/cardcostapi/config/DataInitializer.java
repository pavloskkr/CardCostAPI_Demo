package com.etraveli.cardcostapi.config;

import com.etraveli.cardcostapi.model.CardClearingCost;
import com.etraveli.cardcostapi.service.ClearingCostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private final ClearingCostService clearingCostService;

    public DataInitializer(ClearingCostService clearingCostService) {
        this.clearingCostService = clearingCostService;
    }

    // Bean to preload the data as in the assignment's matrix
    @Bean
    public CommandLineRunner preloadData() {
        return args -> {
            clearingCostService.saveClearingCost(new CardClearingCost("US", 5.0));
            clearingCostService.saveClearingCost(new CardClearingCost("GR", 15.0));
            clearingCostService.saveClearingCost(new CardClearingCost("Others", 10.0));
        };
    }
}
