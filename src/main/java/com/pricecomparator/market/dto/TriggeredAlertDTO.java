package com.pricecomparator.market.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
public class TriggeredAlertDTO {
    private String productName;
    private String supermarketName;
    private BigDecimal currentPrice;
    private BigDecimal targetPrice;
    private LocalDate date;
    private String currency;

    public TriggeredAlertDTO(String productName, String supermarketName, BigDecimal currentPrice,
                             BigDecimal targetPrice, LocalDate date, String currency) {
        this.productName = productName;
        this.supermarketName = supermarketName;
        this.currentPrice = currentPrice;
        this.targetPrice = targetPrice;
        this.date = date;
        this.currency = currency;
    }

}