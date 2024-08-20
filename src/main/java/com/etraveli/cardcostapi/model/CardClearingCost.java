package com.etraveli.cardcostapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardClearingCost implements Serializable {
    private String countryCode;
    private double clearingCost;
}
