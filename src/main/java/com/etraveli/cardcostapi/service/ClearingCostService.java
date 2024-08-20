package com.etraveli.cardcostapi.service;

import com.etraveli.cardcostapi.exception.CountryCodeNotFoundException;
import com.etraveli.cardcostapi.exception.ValidationException;
import com.etraveli.cardcostapi.model.CardClearingCost;
import com.etraveli.cardcostapi.repository.CardClearingCostRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ClearingCostService {

    private final CardClearingCostRepository repository;

    public ClearingCostService(CardClearingCostRepository repository) {
        this.repository = repository;
    }

    public void saveClearingCost(CardClearingCost ccc) {
        // exclude the "Others" entry and validate for the ISO2 country codes
        if (!"Others".equals(ccc.getCountryCode()) && !ccc.getCountryCode().matches("^[A-Z]{2}$")) {
            throw new ValidationException("Country code must be a valid ISO2 code (two uppercase letters) or 'Others'");
        }
        if (ccc.getClearingCost() < 0) {
            throw new ValidationException("Clearing cost can't be less than 0");
        }
        repository.save(ccc);
    }

    public CardClearingCost getClearingCost(String countryCode) {
        CardClearingCost clearingCost = repository.findByCountryCode(countryCode);
        if (clearingCost == null) {
           throw new CountryCodeNotFoundException(countryCode);
        }
        return clearingCost;
    }

    public void deleteClearingCost(String countryCode) {
        if (repository.findByCountryCode(countryCode) == null) {
            throw new CountryCodeNotFoundException(countryCode);
        }
        repository.deleteByCountryCode(countryCode);
    }

    public Map<String, CardClearingCost> getAllClearingCosts() {
        return repository.findAll();
    }

    public void updateClearingCost(CardClearingCost ccc) {
        if (repository.findByCountryCode(ccc.getCountryCode()) == null) {
            throw new CountryCodeNotFoundException(ccc.getCountryCode());
        }
        if (ccc.getClearingCost() < 0) {
            throw new ValidationException("Clearing cost can't be less than 0");
        }
        repository.save(ccc);
    }

    public double getClearingCostFromCountryCode(String alpha2) {
        CardClearingCost clearingCost = repository.findByCountryCode(alpha2);
        if (clearingCost != null) {
            return clearingCost.getClearingCost();
        }
        // return the default (others) cost
        return repository.findByCountryCode("Others").getClearingCost();
    }
}
