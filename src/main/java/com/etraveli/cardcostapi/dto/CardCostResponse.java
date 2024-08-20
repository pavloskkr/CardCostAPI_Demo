package com.etraveli.cardcostapi.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonPropertyOrder({"country", "cost"})
public class CardCostResponse {

    private String country;
    private double cost;

    public CardCostResponse(String country, double cost) {
        this.country = country;
        this.cost = cost;
    }

}
