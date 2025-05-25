package com.pricecomparator.market.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ActiveAlertDTO {
    private String productName;
    private String email;
    private BigDecimal targetPrice;

    public ActiveAlertDTO(String productName, String email, BigDecimal targetPrice) {
        this.productName = productName;
        this.email = email;
        this.targetPrice = targetPrice;
    }

}