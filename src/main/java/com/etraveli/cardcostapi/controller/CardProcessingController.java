package com.etraveli.cardcostapi.controller;

import com.etraveli.cardcostapi.dto.CardCostResponse;
import com.etraveli.cardcostapi.dto.ErrorResponse;
import com.etraveli.cardcostapi.exception.ExternalApiException;
import com.etraveli.cardcostapi.service.CardProcessingService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CardProcessingController {

    private final CardProcessingService cardProcessingService;

    public CardProcessingController(CardProcessingService cardProcessingService) {
        this.cardProcessingService = cardProcessingService;
    }

    // endpoint to calculate a card's clearing cost
    @PostMapping("/payment-card-cost")
    public ResponseEntity<?> getCardClearingCost(@RequestBody Map<String, Object> req) {
        String pan = (String) req.get("card_number");
        if (pan == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid Request", "Card number is required"));
        }

        try {
            CardCostResponse res = cardProcessingService.calculateClearingCost(pan);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException  e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (ExternalApiException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse("Service Unavailable", "Failed to fetch card information"));
        }
    }
}
