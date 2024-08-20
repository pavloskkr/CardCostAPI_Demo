package com.etraveli.cardcostapi.service;

import com.etraveli.cardcostapi.dto.CardCostResponse;
import com.etraveli.cardcostapi.exception.ExternalApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CardProcessingService {

    private final ClearingCostService clearingCostService;
    private final RestTemplate restTemplate;

    public CardProcessingService(ClearingCostService clearingCostService, RestTemplate restTemplate) {
        this.clearingCostService = clearingCostService;
        this.restTemplate = restTemplate;
    }

    public CardCostResponse calculateClearingCost(String pan) {
        // PAN Validation check
        if (pan.length() < 8 || pan.length() > 19) {
            throw new IllegalArgumentException("Card number's length must be between 8 and 19");
        }
        //Extracting the first 6 digits (BIN)
        String bin = pan.substring(0, 6);

        String binlistUrl = "https://lookup.binlist.net/" + bin;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(binlistUrl, Map.class);
            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new ExternalApiException("Failed to fetch card information");
            }

            //alpha2 (ISO2 abbr.) extraction
            String alpha2 = (String) ((Map) response.getBody().get("country")).get("alpha2");

            //calculate clearing cost based on the country code
            double cost = clearingCostService.getClearingCostFromCountryCode(alpha2);

            return new CardCostResponse(alpha2, cost);
        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new ExternalApiException("The external service is busy. Please try again later.");
        } catch (RestClientException e) {
            throw new ExternalApiException("Failed to fetch card information");
        }
    }
}
