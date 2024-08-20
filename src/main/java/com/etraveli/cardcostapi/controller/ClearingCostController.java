package com.etraveli.cardcostapi.controller;

import com.etraveli.cardcostapi.model.CardClearingCost;
import com.etraveli.cardcostapi.service.ClearingCostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/clearing-costs")
public class ClearingCostController {

    private final ClearingCostService clearingCostService;

    public ClearingCostController(ClearingCostService clearingCostService) {
        this.clearingCostService = clearingCostService;
    }

    // endpoints for simple CRUD operations on CardClearingCost

    @PostMapping
    public ResponseEntity<Void> saveClearingCost(@RequestBody CardClearingCost ccc) {
        clearingCostService.saveClearingCost(ccc);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{countryCode}")
    public ResponseEntity<CardClearingCost> getClearingCost(@PathVariable String countryCode) {
        CardClearingCost ccc = clearingCostService.getClearingCost(countryCode);
        return ccc != null ? ResponseEntity.ok(ccc) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{countryCode}")
    public ResponseEntity<Void> deleteClearingCost(@PathVariable String countryCode) {
        clearingCostService.deleteClearingCost(countryCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, CardClearingCost>> getAllClearingCosts() {
        return ResponseEntity.ok(clearingCostService.getAllClearingCosts());
    }

    @PutMapping("/{countryCode}")
    public ResponseEntity<Void> updateClearingCost(@PathVariable String countryCode, @RequestBody CardClearingCost ccc) {

        if (!countryCode.equals(ccc.getCountryCode())) {
            return ResponseEntity.badRequest().build();
        }

        clearingCostService.updateClearingCost(ccc);
        return ResponseEntity.ok().build();
    }
}
