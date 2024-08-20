package com.etraveli.cardcostapi.exception;

public class CountryCodeNotFoundException extends RuntimeException{
    public CountryCodeNotFoundException(String countryCode) {
        super("Country code not found: " + countryCode);
    }
}
